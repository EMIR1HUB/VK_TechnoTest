package com.texhnotest.vk.services;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CacheManager {
  private final Map<String, Object> cache = new ConcurrentHashMap<>();

  public void put(String key, Object value) {
    cache.put(key, value);
  }

  public Optional<Object> get(String key) {
    return Optional.ofNullable(cache.get(key));
  }

  public void evict(String key) {
    cache.remove(key);
  }
}