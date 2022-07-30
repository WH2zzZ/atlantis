package com.oowanghan.atlantis.framework.auth.annotation;

/**
 * @Author WangHan
 * @Create 2021/3/7 2:31 下午
 */
public class AuthCacheConstant {
    public static final String REDISKEY_REGISTER_VERIFYCODE_PREFIX = "app:user:register:verifycode:";
    public static final String REDISKEY_REGISTER_VERIFYCODE_TIME = "app:user:register:verifycode:time:";

    public static final String REDISKEY_LOGIN_VERIFYCODE_PREFIX = "app:user:login:verifycode:";
    public static final String REDISKEY_LOGIN_VERIFYCODE_TIME = "app:user:login:verifycode:time:";

    public static final String REDISKEY_FORGET_VERIFYCODE_PREFIX = "app:user:forget:verifycode:";
    public static final String REDISKEY_FORGET_VERIFYCODE_TIME = "app:user:forget:verifycode:time:";

    public static final String REDIS_KEY_JWT_TOKEN_PRFIX_APP = "app:user:jwt-token:";
}
