package com.sapient.internal.exercise.service;

import com.sapient.internal.exercise.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserCacheService implements CacheService<String, UserDto> {

    @Autowired
    private RedisTemplate<String, UserDto> redisTemplate;

    @Override
    public void saveInValueOperations(String key, UserDto userDto) {
        redisTemplate.opsForValue().set(key, userDto);
    }

    @Override
    public UserDto fetchFromValueOperations(String key) {
        return redisTemplate.opsForValue().get(key);
    }
}
