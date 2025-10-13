package com.beanny.demo.common.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class RedisConfig {
    
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        // Configure JSON serializer for Redis
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.activateDefaultTyping(
                BasicPolymorphicTypeValidator.builder()
                        .allowIfSubType(Object.class)
                        .build(),
                ObjectMapper.DefaultTyping.NON_FINAL
        );
        
        GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);
        
        // default configuration
        RedisCacheConfiguration defaultConfig = this.getRedisCacheConfig(30,jsonSerializer);
        
        // pagination configuration
        RedisCacheConfiguration paginatedConfig = this.getRedisCacheConfig(5,jsonSerializer);
        
        // paginated cache with 5mins TTL
        Map<String,RedisCacheConfiguration> paginationCacheConfigs = new HashMap<>();
        
        paginationCacheConfigs.put("products-paginated", paginatedConfig);
        
        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(paginationCacheConfigs)
                .build();
    }
    
    private RedisCacheConfiguration getRedisCacheConfig(Integer duration, GenericJackson2JsonRedisSerializer jsonSerializer) {
        return RedisCacheConfiguration
                .defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(duration))
                .computePrefixWith(cacheName -> cacheName + ":")
                .serializeKeysWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer())
                )
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(jsonSerializer)
                )
                .disableCachingNullValues();
    }
}
