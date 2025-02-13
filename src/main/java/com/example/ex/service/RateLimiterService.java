package com.example.ex.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Set;

@Service
public class RateLimiterService {
    private final RedisTemplate<String, String> redisTemplate;
    private final ZSetOperations<String, String> zSetOperations;

    public RateLimiterService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.zSetOperations = redisTemplate.opsForZSet();
    }

    public boolean isAllowed(String userId, int limit, int windowSizeSeconds) {
        String key = "rate:" + userId;
        long now = Instant.now().toEpochMilli();
        long windowStart = now - (windowSizeSeconds * 1000);

        // to remove old/expired requests
        zSetOperations.removeRangeByScore(key, 0, windowStart);

        // to get the current request count
        Long requestCount = zSetOperations.zCard(key);
        if (requestCount != null && requestCount >= limit) {
            return false;
        }

        // to add new request
        zSetOperations.add(key, String.valueOf(now), now);

        // expiry is set to slightly longer than window
        redisTemplate.expire(key, windowSizeSeconds + 1, java.util.concurrent.TimeUnit.SECONDS);

        return true; // request allowed
    }
}
