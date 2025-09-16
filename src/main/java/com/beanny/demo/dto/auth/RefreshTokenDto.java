package com.beanny.demo.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RefreshTokenDto {
    @NotNull(message = "refresh token is required")
    @NotEmpty(message = "refresh token should not be emptied")
    @JsonProperty("refresh_token")
    private String refreshToken;
}
