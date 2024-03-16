package com.sapient.internal.exercise.service;

public interface CacheService<K, V> {

    void saveInValueOperations(K key, V value);

    V fetchFromValueOperations(K key);
}
