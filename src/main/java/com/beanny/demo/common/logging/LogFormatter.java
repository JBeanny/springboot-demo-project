package com.beanny.demo.common.logging;

import org.springframework.stereotype.Component;

@Component
public class LogFormatter {
    private static final String LOG_FORMAT = "%s:%s | target=%s, method=%s, startTime=%s, endTime=%s, executionTime=%sms";
    
    public String logRequest(String requestId,String target,String method,Long startTime) {
        return String.format(LOG_FORMAT,
                    "REQUEST",
                    requestId,
                    target,
                    method,
                    startTime,
                    0,
                    0
                );
    }
    
    public String logResponse(String requestId,String target,String method,Long startTime,Long endTime) {
        return String.format(LOG_FORMAT,
                "RESPONSE",
                requestId,
                target,
                method,
                startTime,
                endTime,
                endTime - startTime
        );
    }
    
    public String logError(String requestId,String target,String method,Long startTime,Long endTime) {
        return String.format(LOG_FORMAT,
                "ERROR",
                requestId,
                target,
                method,
                startTime,
                endTime,
                endTime - startTime
        );
    }
}
