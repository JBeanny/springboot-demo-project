package com.beanny.demo.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RefreshTokenResponseDto extends AuthResponseDto{
    @JsonProperty("token_type")
    private String tokenType;
    
    public RefreshTokenResponseDto(String accessToken,String refreshToken,String tokenType) {
        super(accessToken,refreshToken);
        this.tokenType = tokenType;
    }
}
