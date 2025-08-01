package com.beanny.demo.exception;

import com.beanny.demo.exception.model.DuplicateResourceException;
import com.beanny.demo.exception.model.ResourceNotFoundException;
import com.beanny.demo.model.BaseResponseModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<BaseResponseModel> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new BaseResponseModel("fail",ex.getMessage()));
    }
    
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<BaseResponseModel> handleDuplicateResourceException(DuplicateResourceException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new BaseResponseModel("fail",ex.getMessage()));
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponseModel> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new BaseResponseModel("fail","unexpected error occured: " + ex.getMessage()));
    }
}
