package com.oowanghan.atlantis.framework.auth.config;

import com.oowanghan.atlantis.framework.auth.JwtAuthFilter;
import com.oowanghan.atlantis.framework.auth.JwtAuthInterceptor;
import com.oowanghan.atlantis.framework.auth.JwtTokenProcesser;
import com.oowanghan.atlantis.framework.auth.properties.JwtProperties;
import org.redisson.api.RedissonClient;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Jwt 自动化配置
 * @Author WangHan
 * @Create 2022/7/31 17:32
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(JwtProperties.class)
@AutoConfigureAfter(RedissonAutoConfiguration.class)
public class JwtAuthAutoConfiguration {

    private final JwtProperties jwtProperties;
    private final RedissonClient redissonClient;

    public JwtAuthAutoConfiguration(JwtProperties jwtProperties,
                                    RedissonClient redissonClient) {
        this.jwtProperties = jwtProperties;
        this.redissonClient = redissonClient;
    }

    @Bean
    public JwtAuthInterceptor getJwtAuthInterceptor() {
        return new JwtAuthInterceptor();
    }

    @Bean
    public JwtTokenProcesser getJwtTokenProcesser() {
        return new JwtTokenProcesser(jwtProperties, redissonClient);
    }
}
