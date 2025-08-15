package com.beanny.demo.common;

import com.beanny.demo.dto.base.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Utility class to wrap service responses into HTTP ResponseEntity with appropriate status codes.
 * This class bridges the gap between service layer (business logic) and controller layer (HTTP concerns).
 */
public class ResponseWrapper {
    
    private ResponseWrapper() {
        // Utility class - prevent instantiation
    }
    
    // Success responses
    public static <T> ResponseEntity<Response<T>> ok(String message, T data) {
        return ResponseEntity.ok(Response.success(message, data));
    }
    
    public static ResponseEntity<Response<Void>> ok(String message) {
        return ResponseEntity.ok(Response.success(message));
    }
    
    public static <T> ResponseEntity<Response<T>> ok(T data) {
        return ResponseEntity.ok(Response.success(data));
    }
    
    public static <T> ResponseEntity<Response<T>> created(String message, T data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(Response.success(message, data));
    }
    
    public static ResponseEntity<Response<Void>> created(String message) {
        return ResponseEntity.status(HttpStatus.CREATED).body(Response.success(message));
    }
    
    // Error responses
    public static ResponseEntity<Response<Void>> badRequest(String message) {
        return ResponseEntity.badRequest().body(Response.error(message));
    }
    
    public static <T> ResponseEntity<Response<T>> badRequest(String message, T errors) {
        return ResponseEntity.badRequest().body(Response.validationError(message, errors));
    }
    
    public static ResponseEntity<Response<Void>> notFound(String message) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Response.notFound(message));
    }
    
    public static ResponseEntity<Response<Void>> conflict(String message) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Response.conflict(message));
    }
    
    public static ResponseEntity<Response<Void>> unprocessableEntity(String message) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(Response.error(message));
    }
    
    public static ResponseEntity<Response<Void>> internalServerError(String message) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Response.error(message));
    }
}