package com.beanny.demo.controller;

import com.beanny.demo.dto.auth.AuthDto;
import com.beanny.demo.dto.auth.AuthResponseDto;
import com.beanny.demo.dto.auth.RefreshTokenDto;
import com.beanny.demo.dto.auth.RefreshTokenResponseDto;
import com.beanny.demo.dto.base.Response;
import com.beanny.demo.dto.user.UserDto;
import com.beanny.demo.service.security.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    
    @PostMapping("/register")
    public ResponseEntity<Response> register(@Valid @RequestBody UserDto payload) {
        AuthResponseDto dto = authService.register(payload);
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Response.success("201","success","successfully registered user",dto));
    }
    
    @PostMapping("/login")
    public ResponseEntity<Response> login(@RequestBody AuthDto payload) {
        AuthResponseDto dto = authService.login(payload);
        
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.success("200","success","successfully logged in",dto));
    }
    
    @PostMapping("/refresh")
    public ResponseEntity<Response> refresh(@Valid @RequestBody RefreshTokenDto payload) {
        RefreshTokenResponseDto dto = authService.refreshToken(payload);
        
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.success("200","success","successfully refreshed token",dto));
    }
}
