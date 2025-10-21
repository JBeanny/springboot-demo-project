package com.beanny.demo.service;

import com.beanny.demo.common.config.ApplicationConfiguration;
import com.beanny.demo.common.wrapper.WebClientWrapper;
import com.beanny.demo.dto.external.JsonPlaceholderCommentDto;
import com.beanny.demo.dto.external.JsonPlaceholderPostDto;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class JsonPlaceholderService {
    @Autowired
    private WebClientWrapper webClientWrapper;
    
    @Autowired
    private ApplicationConfiguration appConfig;
    
    private String BASE_URL;
    private String POSTS_URI;
    private String COMMENTS_URI;
    
    @PostConstruct
    private void init() {
        this.BASE_URL = appConfig.getJsonPlaceholder().getBaseUrl();
        this.POSTS_URI = appConfig.getJsonPlaceholder().getPostsUri();
        this.COMMENTS_URI = appConfig.getJsonPlaceholder().getCommentsUri();
    }
    
    public List<JsonPlaceholderPostDto> getPosts() {
        String url = BASE_URL.concat(POSTS_URI);
        
        List<JsonPlaceholderPostDto> response = (List<JsonPlaceholderPostDto>) webClientWrapper.getSync(url, List.class);
        
        return response;
    }
    
    public List<JsonPlaceholderCommentDto> getComments() {
        String url = BASE_URL.concat(COMMENTS_URI);
        
        List<JsonPlaceholderCommentDto> response = (List<JsonPlaceholderCommentDto>) webClientWrapper.getSync(url, List.class);
        
        return response;
    }
}
