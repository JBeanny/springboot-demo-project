package com.beanny.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Configuration class for service layer setup.
 * Enables transaction management and other service-related configurations.
 */
@Configuration
@EnableTransactionManagement
public class ServiceConfig {
    
    // Additional service layer configurations can be added here
    // For example: custom transaction managers, aspect configurations, etc.
}