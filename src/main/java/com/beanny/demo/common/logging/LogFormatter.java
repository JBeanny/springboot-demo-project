package com.beanny.demo.common.logging;

import com.beanny.demo.common.constant.LoggingConstant;
import org.springframework.stereotype.Component;

@Component
public class LogFormatter {
    private static final String LOG_FORMAT = "%s:%s | target=%s, method=%s, httpMethod=%s, requestPath=%s, startTime=%s, endTime=%s, executionTime=%sms";
    
    public String logRequest(String requestId,String target,String method,Long startTime,String httpMethod,String requestPath) {
        return String.format(LOG_FORMAT,
                    LoggingConstant.REQUEST,
                    requestId,
                    target,
                    method,
                    httpMethod,
                    requestPath,
                    startTime,
                    0,
                    0
                );
    }
    
    public String logResponse(String requestId,String target,String method,Long startTime,Long endTime,String httpMethod,String requestPath) {
        return String.format(LOG_FORMAT,
                LoggingConstant.RESPONSE,
                requestId,
                target,
                method,
                httpMethod,
                requestPath,
                startTime,
                endTime,
                endTime - startTime
        );
    }
    
    public String logError(String requestId,String target,String method,Long startTime,Long endTime,String httpMethod,String requestPath) {
        return String.format(LOG_FORMAT,
                LoggingConstant.ERROR,
                requestId,
                target,
                method,
                httpMethod,
                requestPath,
                startTime,
                endTime,
                endTime - startTime
        );
    }
}
