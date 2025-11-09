package com.transport.service.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisServiceImpl<K, F, V> implements RedisService<K, F, V> {

    private final RedisTemplate<K, V> redisTemplate;
    private final HashOperations<K, F, V> hashOperations;

    public RedisServiceImpl(RedisTemplate<K, V> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public void setValue(K key, V value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void setTimeToLive(K key, long timeoutInDay) {
        redisTemplate.expire(key, timeoutInDay, TimeUnit.DAYS);
    }

    @Override
    public void hashSet(K key, F field, V value) {
        hashOperations.put(key, field, value);
    }

    @Override
    public boolean hashExists(K key, F field) {
        return hashOperations.hasKey(key, field);
    }

    @Override
    public V get(K key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public Map<F, V> getAllHash(K key) {
        return hashOperations.entries(key);
    }

    @Override
    public V hashGet(K key, F field) {
        return hashOperations.get(key, field);
    }

    @Override
    public List<V> hashGetByFieldPrefix(K key, String fieldPrefix) {
        List<V> result = new ArrayList<>();
        Map<F, V> hashEntries = hashOperations.entries(key);

        for (Map.Entry<F, V> entry : hashEntries.entrySet()) {
            if (entry.getKey().toString().startsWith(fieldPrefix)) {
                result.add(entry.getValue());
            }
        }
        return result;
    }

    @Override
    public Set<F> getFieldPrefixes(K key) {
        return hashOperations.keys(key);
    }

    @Override
    public void delete(K key) {
        redisTemplate.delete(key);
    }

    @Override
    public void delete(K key, F field) {
        hashOperations.delete(key, field);
    }

    @Override
    public void delete(K key, List<F> fields) {
        for (F field : fields) {
            hashOperations.delete(key, field);
        }
    }

    @Override
    public void deleteByPattern(K pattern) {
        Set<K> keys = redisTemplate.keys(pattern);
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }
}
