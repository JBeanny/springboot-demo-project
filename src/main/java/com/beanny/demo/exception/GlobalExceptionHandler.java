package com.beanny.demo.exception;

import com.beanny.demo.common.ResponseWrapper;
import com.beanny.demo.dto.base.Response;
import com.beanny.demo.exception.model.DuplicateResourceException;
import com.beanny.demo.exception.model.ResourceNotFoundException;
import com.beanny.demo.exception.model.UnprocessableEntityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler that catches exceptions across the application
 * and converts them to standardized API responses using the Response wrapper.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Response<Void>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        logger.warn("Resource not found: {}", ex.getMessage());
        return ResponseWrapper.notFound(ex.getMessage());
    }
    
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<Response<Void>> handleDuplicateResourceException(DuplicateResourceException ex) {
        logger.warn("Duplicate resource: {}", ex.getMessage());
        return ResponseWrapper.conflict(ex.getMessage());
    }
    
    @ExceptionHandler(UnprocessableEntityException.class)
    public ResponseEntity<Response<Void>> handleUnprocessableEntity(UnprocessableEntityException ex) {
        logger.warn("Unprocessable entity: {}", ex.getMessage());
        return ResponseWrapper.unprocessableEntity(ex.getMessage());
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<Void>> handleGenericException(Exception ex) {
        logger.error("Unexpected error occurred", ex);
        return ResponseWrapper.internalServerError("An unexpected error occurred: " + ex.getMessage());
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response<Map<String, String>>> handleValidationException(MethodArgumentNotValidException ex) {
        logger.warn("Validation failed: {}", ex.getMessage());
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });
        
        return ResponseWrapper.badRequest("Validation failed", errors);
    }
}
