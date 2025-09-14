package com.beanny.demo.service.security;

import com.beanny.demo.entity.RefreshToken;
import com.beanny.demo.entity.User;
import com.beanny.demo.exception.model.ResourceNotFoundException;
import com.beanny.demo.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    
    public RefreshToken createRefreshToken(User user) {
        // TODO: generate as Base64 URL Encoder
        String refreshToken = UUID.randomUUID().toString();
        
        RefreshToken entity = new RefreshToken();
        entity.setToken(refreshToken);
        entity.setExpiresAt(LocalDateTime.now().plusHours(3));
        entity.setUser(user);
        
        return refreshTokenRepository.save(entity);
    }
    
    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Refresh token is invalid"));
    }
    
    public RefreshToken verifyToken(RefreshToken token) throws AuthenticationException {
        // expiration && revoke
        if(!token.isValid()) {
            refreshTokenRepository.delete(token);
            throw new AuthenticationException("Refresh token was expired or revoked");
        }
        
        return token;
    }
    
    public RefreshToken rotateRefreshToken(RefreshToken oldToken) {
        // revoke old refresh token
        oldToken.setRevoked(true);
        refreshTokenRepository.save(oldToken);
        
        // generate new refresh token
        return this.createRefreshToken(oldToken.getUser());
    }
}
