package com.transport.service.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RedisService<K, F, V> {

    // Set giá trị thông thường
    void setValue(K key, V value);

    // Set thời gian sống cho 1 key (tính bằng ngày)
    void setTimeToLive(K key, long timeoutInDay);

    // Set giá trị trong hash (giống HSET)
    void hashSet(K key, F field, V value);

    // Kiểm tra field có tồn tại trong hash không
    boolean hashExists(K key, F field);

    // Lấy giá trị thông thường
    V get(K key);

    // Lấy toàn bộ field-value của 1 hash
    Map<F, V> getAllHash(K key);

    // Lấy 1 field cụ thể trong hash
    V hashGet(K key, F field);

    // Lấy danh sách các value có field bắt đầu bằng prefix
    List<V> hashGetByFieldPrefix(K key, String fieldPrefix);

    // Lấy danh sách các field có prefix
    Set<F> getFieldPrefixes(K key);

    // Xóa key
    void delete(K key);

    // Xóa 1 field trong hash
    void delete(K key, F field);

    // Xóa nhiều field trong hash
    void delete(K key, List<F> fields);

    void deleteByPattern(K pattern);
}
