package com.beanny.demo.service;

import com.beanny.demo.common.wrapper.WebClientWrapper;
import com.beanny.demo.dto.external.JsonPlaceholderCommentDto;
import com.beanny.demo.dto.external.JsonPlaceholderPostDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class JsonPlaceholderService {
    @Autowired
    private WebClientWrapper webClientWrapper;
    
    public List<JsonPlaceholderPostDto> getPosts() {
        String uri = "https://jsonplaceholder.typicode.com/posts";
        
        List<JsonPlaceholderPostDto> response = (List<JsonPlaceholderPostDto>) webClientWrapper.getSync(uri, List.class);
        
        return response;
    }
    
    public List<JsonPlaceholderCommentDto> getComments() {
        String uri = "https://jsonplaceholder.typicode.com/comments";
        
        List<JsonPlaceholderCommentDto> response = (List<JsonPlaceholderCommentDto>) webClientWrapper.getSync(uri, List.class);
        
        return response;
    }
}
