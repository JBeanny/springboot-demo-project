# Step 1: Build
FROM maven:3.9-eclipse-temurin-21-alpine AS builder

# Set working directory
WORKDIR /app

# Copy maven configurations file
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

# Download dependencies
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Step 2: Runtime
# jre = Java Runtime Environment
FROM eclipse-temurin:21-jre-alpine

# Add metadata (optional)
LABEL maintainer="beanny"
LABEL description="Spring Boot Demo Application"
LABEL version="0.0.1-SNAPSHOT"

# Create a non-root user for security
# create "spring" group -> create "spring" user -> add "spring" user to "spring" group
RUN addgroup -S spring && adduser -S spring -G spring

# Set working directory
WORKDIR /app

# Copy the built jar from build step
COPY --from=builder /app/target/*.jar app.jar

# change ownership for non-root user
RUN chown -R spring:spring /app

# Switch user to "spring"
USER spring

# Expose default port of Spring Boot application
EXPOSE 8080

# Set default profile for Spring Boot Application
ENV SPRING_PROFILES_ACTIVE=prod

# Health check for application
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java","-XX:+UseContainerSupport","-XX:MaxRAMPercentage=75.0","-Djava.security.egd=file:/dev/./urandom","-jar","app.jar"]