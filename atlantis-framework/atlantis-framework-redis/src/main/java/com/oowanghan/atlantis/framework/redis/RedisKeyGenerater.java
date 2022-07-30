package com.oowanghan.atlantis.framework.redis;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.oowanghan.atlantis.util.tool.constant.SymbolConstant;
import org.springframework.core.env.Environment;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * redis-key生成器
 *
 * @Author WangHan
 * @Create 2022/7/30 18:40
 */
public class RedisKeyGenerater {

    public static final String DEFAULT = "default";
    public static final String SPRING_APPLICATION_NAME = "spring.application.name";

    public static String generateRedisKey(String model, String name) {
        Environment environment = SpringUtil.getApplicationContext().getEnvironment();
        String applicationName = environment.getProperty(SPRING_APPLICATION_NAME);
        if (StrUtil.isBlank(applicationName)) {
            applicationName = DEFAULT;
        }
        return Arrays.asList(applicationName, model, name)
                .stream().collect(Collectors.joining(SymbolConstant.DELIMITER));
    }

    public static String generateRedisKey(String... keys) {
        Environment environment = SpringUtil.getApplicationContext().getEnvironment();
        String applicationName = environment.getProperty(SPRING_APPLICATION_NAME);
        if (StrUtil.isBlank(applicationName)) {
            applicationName = DEFAULT;
        }
        String redisKey = Arrays.stream(keys).collect(Collectors.joining(SymbolConstant.DELIMITER));
        return applicationName + SymbolConstant.DELIMITER + redisKey;
    }
}
