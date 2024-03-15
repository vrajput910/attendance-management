package com.sapient.internal.exercise.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LocalCacheService {

    private final Map<String, Object> cache = new HashMap<>();

    public void saveInCache(String key, Object value) {
        cache.put(key, value);
    }

    public Object fetchFromCache(String key) {
        return cache.get(key);
    }
}
