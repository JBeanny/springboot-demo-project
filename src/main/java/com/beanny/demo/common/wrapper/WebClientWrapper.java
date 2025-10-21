package com.beanny.demo.common.wrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

import java.time.Duration;

@Component
public class WebClientWrapper {
    @Autowired
    private WebClient webClient;
    
    public <T> T getSync(String url, Class<T> responseType) {
        return webClient
                .get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(responseType)
                .timeout(Duration.ofMillis(5000))
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(1)))
                .block();
    }
}
