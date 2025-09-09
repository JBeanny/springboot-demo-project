package com.beanny.demo.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AuthDto {
    @NotNull(message = "username is required")
    @NotBlank(message = "username must not be blank")
    private String username;
    
    @NotNull(message = "password is required")
    @NotBlank(message = "password must not be blank")
    private String password;
}
