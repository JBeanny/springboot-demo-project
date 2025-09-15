package com.beanny.demo.service.security;

import com.beanny.demo.entity.RefreshToken;
import com.beanny.demo.entity.User;
import com.beanny.demo.exception.model.ResourceNotFoundException;
import com.beanny.demo.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
public class RefreshTokenService {
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    
    @Value("${config.security.refresh-token-expiration}")
    private Long expiration;
    
    public RefreshToken createRefreshToken(User user) {
        String refreshToken = this.generateSecureRefreshToken();
        
        RefreshToken entity = new RefreshToken();
        entity.setToken(refreshToken);
        entity.setExpiresAt(LocalDateTime.now().plusHours(this.expiration));
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
    
    private String generateSecureRefreshToken() {
        // -128 to 127
        SecureRandom random = new SecureRandom();
        
        // array bytes of 64 length , 512 bits
        byte[] tokenBytes = new byte[64];
        
        // make each byte has its own secure value
        // [67,-125,100,12,....]
        random.nextBytes(tokenBytes);
        
        // "hwdj1e0slasf3f3/+asjdfasjd/+" not URL friendly
        // "hashfafej_-asdfkjake" URL friendly
        return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
    }
}
