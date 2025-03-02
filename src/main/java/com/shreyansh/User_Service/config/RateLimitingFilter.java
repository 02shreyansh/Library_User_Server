package com.shreyansh.User_Service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    @Autowired
    private RateLimitingService rateLimitingService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        // Only apply to auth endpoints
        String path = request.getRequestURI();
        if (path.startsWith("/auth/")) {
            String clientIp = getClientIP(request);
            
            // Check if the request is allowed
            if (!rateLimitingService.isAllowed(clientIp, path)) {
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.setContentType("application/json");
                
                Map<String, Object> errorDetails = new HashMap<>();
                errorDetails.put("status", false);
                errorDetails.put("message", "Rate limit exceeded. Please try again later.");
                
                response.getWriter().write(objectMapper.writeValueAsString(errorDetails));
                return;
            }
        }
        
        filterChain.doFilter(request, response);
    }

    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // Apply only to auth endpoints
        String path = request.getRequestURI();
        return !path.startsWith("/auth/");
    }
}
