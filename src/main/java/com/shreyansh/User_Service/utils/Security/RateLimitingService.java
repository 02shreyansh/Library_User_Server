package com.shreyansh.User_Service.utils.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RateLimitingService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    private static final int MAX_REQUESTS_PER_IP = 10; 
    private static final int RATE_LIMIT_WINDOW = 60; 
    
    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final int LOCKOUT_DURATION = 15; 

    /**
     * Check if the request is allowed based on rate limiting
     *
     * @param ip The IP address of the client
     * @param endpoint The endpoint being accessed
     * @return true if request is allowed, false if rate limited
     */
    public boolean isAllowed(String ip, String endpoint) {
        String key = "rate_limit:" + ip + ":" + endpoint;
        
        // Get current count for this IP and endpoint
        Long count = redisTemplate.opsForValue().increment(key, 1);
        
        // If this is the first request in this window, set expiry
        if (count != null && count == 1) {
            redisTemplate.expire(key, RATE_LIMIT_WINDOW, TimeUnit.SECONDS);
        }
        
        return count != null && count <= MAX_REQUESTS_PER_IP;
    }

    /**
     * Record a failed login attempt
     *
     * @param username The username that failed login
     * @return The number of failed attempts
     */
    public int recordFailedLogin(String username) {
        String key = "failed_login:" + username;
        
        // Increment failed attempts
        Long attempts = redisTemplate.opsForValue().increment(key, 1);
        
        // Set expiry if first failure (reset counter after 1 hour)
        if (attempts != null && attempts == 1) {
            redisTemplate.expire(key, 60, TimeUnit.MINUTES);
        }
        
        // If max attempts reached, lock the account
        if (attempts != null && attempts >= MAX_FAILED_ATTEMPTS) {
            lockAccount(username);
        }
        
        return attempts != null ? attempts.intValue() : 0;
    }

    /**
     * Lock an account for the specified duration
     *
     * @param username The username to lock
     */
    private void lockAccount(String username) {
        String key = "account_locked:" + username;
        redisTemplate.opsForValue().set(key, "locked", LOCKOUT_DURATION, TimeUnit.MINUTES);
    }

    /**
     * Reset failed login attempts after successful login
     *
     * @param username The username that logged in successfully
     */
    public void resetFailedAttempts(String username) {
        String key = "failed_login:" + username;
        redisTemplate.delete(key);
    }

    /**
     * Check if an account is locked
     *
     * @param username The username to check
     * @return true if account is locked, false otherwise
     */
    public boolean isAccountLocked(String username) {
        String key = "account_locked:" + username;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
    
    /**
     * Get remaining lockout time in minutes
     *
     * @param username The username to check
     * @return remaining lockout time in minutes, or 0 if not locked
     */
    public long getRemainingLockoutTime(String username) {
        String key = "account_locked:" + username;
        Long expireTime = redisTemplate.getExpire(key, TimeUnit.MINUTES);
        return expireTime != null && expireTime > 0 ? expireTime : 0;
    }
}