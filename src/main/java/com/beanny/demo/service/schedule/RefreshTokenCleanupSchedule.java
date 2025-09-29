package com.beanny.demo.service.schedule;

import com.beanny.demo.service.security.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenCleanupSchedule {
    @Autowired
    private RefreshTokenService refreshTokenService;
    
    @Scheduled(cron = "* * 0/3 * * ?") // every 3 hours from hour 00:00
    public void cleanupExpiredAndRevokedToken() {
        // execute to clean up tokens
        refreshTokenService.deleteExpiredAndRevokedToken();
    }
}
