package com.oowanghan.atlantis.framework.redis;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 分布式事务锁
 *
 * @author ly wanghan
 */
public class DistributedRedisLock {

    private final Logger log = LoggerFactory.getLogger(DistributedRedisLock.class);
    private static final String REDISSON_KEY = "LOCK:";

    private RedissonClient redissonClient;

    public DistributedRedisLock(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    private String getPrefixName() {
        return REDISSON_KEY;
    }

    /**
     * timeout防止死锁.lockName为当前功能所需要上锁的唯一值
     *
     * @Author WangHan
     * @Create 2:45 下午 2019/9/8
     * @Param [lockName, timeout, timeUnit]
     * @Return boolean
     */
    public boolean lock(String lockName, Integer timeout, TimeUnit timeUnit) {
        String lockKey = getPrefixName() + lockName;
        try {
            redissonClient.getLock(lockKey).lock(timeout, timeUnit);
            log.info("[LOCK-SUCCESS] TreadName : {}, RedisKey : {}", Thread.currentThread().getName(), lockKey);
            return true;
        } catch (Exception ex) {
            log.info("[LOCK-ERROR] TreadName : {}, RedisKey : {}, Message : {}", Thread.currentThread().getName(),
                    lockKey, ex.getMessage());
            return false;
        }
    }

    public boolean release(String lockName) {
        String lockKey = getPrefixName() + lockName;
        try {
            redissonClient.getLock(lockKey).unlock();
            log.info("[LOCK-RELEASE-SUCCESS] TreadName : {}, RedisKey : {}", Thread.currentThread().getName(), lockKey);
            return true;
        } catch (Exception ex) {
            log.info("[LOCK-RELEASE-ERROR] TreadName : {}, RedisKey : {}, Message : {}", Thread.currentThread().getName(),
                    lockKey, ex.getMessage());
            return false;
        }
    }

    /**
     * @param lockName
     * @return true 被占用 false 没有占用
     */
    public RLock isLock(String lockName) {
        String lockKey = getPrefixName() + lockName;
        try {
            RLock lock = redissonClient.getLock(lockKey);
            if (!Objects.isNull(lock)) {
                return lock;
            }
            log.info("[IS-LOCK] TreadName : {}, RedisKey : {}", Thread.currentThread().getName(), lockKey);
        } catch (Exception ex) {
            log.info("[IS-LOCK] TreadName : {}, RedisKey : {}, Message : {}", Thread.currentThread().getName(),
                    lockKey, ex.getMessage());
            return null;
        }
        return null;
    }
}
