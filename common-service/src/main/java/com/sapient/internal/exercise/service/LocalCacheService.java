package com.sapient.internal.exercise.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LocalCacheService implements CacheService<String, String> {

    private final Map<String, String> cache = new HashMap<>();

    @Override
    public void saveInValueOperations(String key, String value) {
        cache.put(key, value);
    }

    @Override
    public String fetchFromValueOperations(String key) {
        return cache.get(key);
    }
}
