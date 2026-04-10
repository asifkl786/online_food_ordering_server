package com.ofos.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderNumberGenerator {
    
    private final RedisTemplate<String, String> redisTemplate;
    
    private static final String ORDER_NUMBER_PREFIX = "ORD";
    private static final String DATE_FORMAT = "yyyyMMdd";
    private static final String COUNTER_KEY_PREFIX = "order:counter:";
    
    public String generateOrderNumber() {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT));
        String counterKey = COUNTER_KEY_PREFIX + date;
        
        // Get and increment counter from Redis
        Long counter = redisTemplate.opsForValue().increment(counterKey);
        
        // Set expiry for counter key (end of day)
		/*
		 * if (counter == 1) { redisTemplate.expireAt(counterKey,
		 * java.time.LocalDateTime.now().plusDays(1).toLocalDate().atStartOfDay().
		 * minusNanos(1)); }
		 */
        if (counter == 1) {
            // ✅ Convert LocalDateTime to Date
            LocalDateTime endOfDay = LocalDateTime.now().plusDays(1).toLocalDate().atStartOfDay().minusNanos(1);
            Date expiryDate = Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant());
            redisTemplate.expireAt(counterKey, expiryDate);
        }
        
        String sequence = String.format("%06d", counter);
        String orderNumber = ORDER_NUMBER_PREFIX + date + sequence;
        
        log.debug("Generated order number: {}", orderNumber);
        return orderNumber;
    }
    
    // Alternative implementation without Redis (using database sequence)
    public String generateOrderNumberWithDatabase() {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT));
        // This would require a database sequence or table
        Long sequence = getNextSequenceValue();
        String orderNumber = ORDER_NUMBER_PREFIX + date + String.format("%06d", sequence);
        log.debug("Generated order number: {}", orderNumber);
        return orderNumber;
    }
    
    private Long getNextSequenceValue() {
        // Implement database sequence logic
        // For now, return a timestamp-based value
        return System.currentTimeMillis() % 1000000;
    }
}