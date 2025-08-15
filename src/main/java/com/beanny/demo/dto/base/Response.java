package com.beanny.demo.dto.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

@Getter
@JsonPropertyOrder(value = {"status","message","data","timestamp"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> {
    private final String status;
    private final String message;
    private final T data;
    private final Long timestamp;
    
    private Response(String status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }
    
    // Success responses
    public static <T> Response<T> success(String message, T data) {
        return new Response<>("success", message, data);
    }
    
    public static Response<Void> success(String message) {
        return new Response<>("success", message, null);
    }
    
    public static <T> Response<T> success(T data) {
        return new Response<>("success", "Operation completed successfully", data);
    }
    
    // Error responses
    public static <T> Response<T> error(String message, T data) {
        return new Response<>("error", message, data);
    }
    
    public static Response<Void> error(String message) {
        return new Response<>("error", message, null);
    }
    
    // Specific error types
    public static Response<Void> notFound(String message) {
        return new Response<>("error", message, null);
    }
    
    public static <T> Response<T> validationError(String message, T errors) {
        return new Response<>("validation_error", message, errors);
    }
    
    public static Response<Void> conflict(String message) {
        return new Response<>("error", message, null);
    }
}
