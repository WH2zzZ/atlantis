package com.oowanghan.atlantis.framework.redis;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * SnowflakeUtil生成工具类，注意只支持1024个节点，之后就可能生成重复id
 *
 * @Author WangHan
 * @Create 2020/8/26 4:42 下午
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(DistributeLockAutoConfiguration.class)
public class SnowflakeAutoConfiguration {

    private final Logger log = LoggerFactory.getLogger(SnowflakeAutoConfiguration.class);
    public static final int MAX_WORK_ID = 1023;
    @Value("${spring.application.name}")
    private String applicationName;

    @Bean
    @ConditionalOnBean(DistributedRedisLock.class)
    public Snowflake getSnowflake(DistributedRedisLock distributedRedisLock,
                                  RedissonClient redissonClient) {
        distributedRedisLock.lock("SNOWFLAKE", 5, TimeUnit.SECONDS);

        long snowFlakeId;
        try {
            RAtomicLong snowFlakeIdCache = redissonClient.getAtomicLong("SNOWFLAKE:" + applicationName);
            snowFlakeId = snowFlakeIdCache.getAndIncrement();
            if (snowFlakeId > MAX_WORK_ID) {
                snowFlakeId = 0;
                snowFlakeIdCache.set(0);
            }
        } catch (Exception e) {
            throw new RuntimeException("SnowFlakeId generate is error");
        } finally {
            distributedRedisLock.release("SNOWFLAKE");
        }

        StringBuilder binarySnowFlakeId = new StringBuilder(Long.toBinaryString(snowFlakeId));
        int length = binarySnowFlakeId.length();
        if (length < 10) {
            for (int i = 0; i < 10 - length; i++) {
                binarySnowFlakeId.insert(0, "0");
            }
        }
        String binaryWorkId = binarySnowFlakeId.substring(binarySnowFlakeId.length() - 5);
        String binaryDataId = binarySnowFlakeId.substring(0, binarySnowFlakeId.length() - 5);
        long workId = Long.parseLong(binaryWorkId, 2);
        long dataId = Long.parseLong(binaryDataId, 2);
        log.info("Snowflake workId : {}. dataId : {}", workId, dataId);
        return IdUtil.createSnowflake(workId, dataId);
    }
}
