package com.beanny.demo.common.wrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@Component
public class WebClientWrapper {
    @Autowired
    private WebClient webClient;
    
    public Object getSync(String url) {
        return webClient
                .get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Object.class)
                .timeout(Duration.ofMillis(5000))
                .block();
    }
}
