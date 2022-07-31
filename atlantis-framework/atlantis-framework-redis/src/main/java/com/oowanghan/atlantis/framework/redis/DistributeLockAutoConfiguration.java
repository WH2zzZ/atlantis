package com.oowanghan.atlantis.framework.redis;

import org.redisson.api.RedissonClient;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 分布式锁配置类
 * @Author WangHan
 * @Create 2022/7/31 14:57
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(RedissonAutoConfiguration.class)
public class DistributeLockAutoConfiguration {

    @Bean
    @ConditionalOnBean(RedissonClient.class)
    public DistributedRedisLock getDistributeLock(RedissonClient redissonClient) {
        return new DistributedRedisLock(redissonClient);
    }
}
