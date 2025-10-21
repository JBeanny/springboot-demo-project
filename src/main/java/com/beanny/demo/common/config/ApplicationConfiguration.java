package com.beanny.demo.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration
@ConfigurationProperties(prefix = "config")
@Getter
@Setter
public class ApplicationConfiguration {
    private Security security;
    private Pagination pagination;
    private JsonPlaceholder jsonPlaceholder;
    
    @Getter
    @Setter
    public static class Security {
        private String secret;
        private long expiration;
        private long refreshTokenExpiration; // refresh-token-expiration
    }
    
    @Getter
    @Setter
    public static class Pagination {
        private String baseUrl;
        private HashMap<String,String> uri;
        
        public String getUrlByResource(String resource) {
            return baseUrl.concat(uri.getOrDefault(resource,""));
        }
    }
    
    @Getter
    @Setter
    public static class JsonPlaceholder {
        private String baseUrl;
        private HashMap<String,String> uri;
        
        public String getPostsUri() {
            return uri.get("posts");
        }
        
        public String getCommentsUri() {
            return uri.get("comments");
        }
    }
}
