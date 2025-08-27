package com.beanny.demo.common.filter;

import com.beanny.demo.common.constant.RequestConstant;
import jakarta.servlet.*;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
@Order(1)
public class RequestFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        
        try {
            String requestId = UUID.randomUUID().toString();
            MDC.put(RequestConstant.REQUEST_ID,requestId);
            
            // execute our implementation / logic
            filterChain.doFilter(servletRequest,servletResponse);
        } finally {
            MDC.clear();
        }
    }
}
