package com.transport.listener;

import jakarta.persistence.*;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.transport.service.redis.RedisService;

@Component
@Slf4j
public class TripListener {

    private static RedisService<String, String, Object>  redisService;

    // ⚡ Dùng setter static để Spring inject được bean
    @Autowired
    public void setRedisService(RedisService<String, String, Object>  redisService) {
        TripListener.redisService = redisService;
    }

    /** Sau khi thêm mới */
    @PostPersist
    public void afterCreate(Object entity) {
        clearCache("CREATE", entity);
    }

    /** Sau khi cập nhật */
    @PostUpdate
    public void afterUpdate(Object entity) {
        clearCache("UPDATE", entity);
    }

    /** Sau khi xóa */
    @PostRemove
    public void afterDelete(Object entity) {
        clearCache("DELETE", entity);
    }

    /** Xóa cache liên quan */
    private void clearCache(String action, Object entity) {
        if (redisService != null) {
            log.info("🧹 TripListener: " + action + " -> clear Redis cache 'trip:list:*'");
            redisService.deleteByPattern("trip:list:*");
        } else {
            log.warn("⚠️ RedisService chưa được inject vào TripListener");
        }
    }
}