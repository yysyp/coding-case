package com.poc.redis.repository;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.redis.entity.User;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Repository for managing User entities in Redis.
 */
@Repository
public class UserRepository {
    
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;
    private static final String USER_KEY_PREFIX = "user:";
    private static final String ALL_USERS_KEY = "users:all";
    
    public UserRepository(RedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }
    
    /**
     * Save a user to Redis.
     *
     * @param user the user to save
     * @return the saved user
     */
    public User save(User user) {
        try {
            String key = USER_KEY_PREFIX + user.getId();
            String value = objectMapper.writeValueAsString(user);
            
            redisTemplate.opsForValue().set(key, value);
            redisTemplate.opsForSet().add(ALL_USERS_KEY, user.getId());
            
            return user;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error saving user to Redis", e);
        }
    }
    
    /**
     * Find a user by ID.
     *
     * @param id the user ID
     * @return Optional containing the user if found
     */
    public Optional<User> findById(String id) {
        try {
            String key = USER_KEY_PREFIX + id;
            String value = redisTemplate.opsForValue().get(key);
            
            if (value != null) {
                User user = objectMapper.readValue(value, User.class);
                return Optional.of(user);
            }
            
            return Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException("Error finding user in Redis", e);
        }
    }
    
    /**
     * Find all users.
     *
     * @return list of all users
     */
    public List<User> findAll() {
        try {
            return redisTemplate.opsForSet().members(ALL_USERS_KEY)
                    .stream()
                    .map(this::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error finding all users in Redis", e);
        }
    }
    
    /**
     * Delete a user by ID.
     *
     * @param id the user ID
     * @return true if deleted, false otherwise
     */
    public boolean deleteById(String id) {
        try {
            String key = USER_KEY_PREFIX + id;
            Boolean deleted = redisTemplate.delete(key);
            
            if (Boolean.TRUE.equals(deleted)) {
                redisTemplate.opsForSet().remove(ALL_USERS_KEY, id);
                return true;
            }
            
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Error deleting user from Redis", e);
        }
    }
    
    /**
     * Update a user.
     *
     * @param user the user to update
     * @return the updated user
     */
    public User update(User user) {
        user.setUpdatedAt(LocalDateTime.now());
        return save(user);
    }
    
    /**
     * Check if a user exists by ID.
     *
     * @param id the user ID
     * @return true if exists, false otherwise
     */
    public boolean existsById(String id) {
        String key = USER_KEY_PREFIX + id;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
    
    /**
     * Set expiration time for a user record.
     *
     * @param id the user ID
     * @param timeout expiration time in seconds
     */
    public void expire(String id, long timeout) {
        String key = USER_KEY_PREFIX + id;
        redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
    }
}
