package com.beanny.demo.common.wrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Component
public class WebClientWrapper {
    @Autowired
    private WebClient webClient;
    
    public <T> Mono<T> getAsync(String url, Class<T> responseType) {
        return webClient
                .get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, this::handleErrorResponse)
                .bodyToMono(responseType)
                .timeout(Duration.ofMillis(5000))
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(1))
                        .filter(throwable -> throwable instanceof WebClientResponseException
                                && ((WebClientResponseException) throwable).getStatusCode().is5xxServerError()
                        )
                );
    }
    
    public <T> T getSync(String url, Class<T> responseType) {
        return webClient
                .get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, this::handleErrorResponse)
                .bodyToMono(responseType)
                .timeout(Duration.ofMillis(5000))
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(1))
                        .filter(throwable -> throwable instanceof WebClientResponseException
                                && ((WebClientResponseException) throwable).getStatusCode().is5xxServerError()
                        )
                )
                .block();
    }
    
    public <T> T postSync(String url, Object payload, Class<T> responseType) {
        return webClient
                .post()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(payload)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, this::handleErrorResponse)
                .bodyToMono(responseType)
                .timeout(Duration.ofMillis(5000))
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(1))
                        .filter(throwable -> throwable instanceof WebClientResponseException
                                && ((WebClientResponseException) throwable).getStatusCode().is5xxServerError()
                        )
                )
                .block();
    }
    
    private Mono<Throwable> handleErrorResponse(ClientResponse response) {
        return response.bodyToMono(String.class)
                .flatMap(body -> {
                    String errorMsg = "Client error: " + response.statusCode();
                    
                    return Mono.error(
                            new WebClientResponseException(
                                    errorMsg,
                                    response.statusCode().value(), // 401 ,403, 404, 400
                                    response.statusCode().toString(),// 401 Unauthorized, 404 Not Found
                                    null,
                                    body.getBytes(),
                                    null
                            )
                    );
                });
    }
}
