package com.beanny.demo.service;

import com.beanny.demo.common.wrapper.WebClientWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JsonPlaceholderService {
    @Autowired
    private WebClientWrapper webClientWrapper;
    
    public Object testSyncApi() {
        String uri = "https://jsonplaceholder.typicode.com/posts";
        
        log.info("TEST: Before API request");
        
        Object response = webClientWrapper.getSync(uri);
        
        log.info("TEST: Getting response");
        
        log.info("TEST: After API request");
        
        return response;
    }
}
