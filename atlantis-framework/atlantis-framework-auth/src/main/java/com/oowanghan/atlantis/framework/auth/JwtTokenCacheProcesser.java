package com.oowanghan.atlantis.framework.auth;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.oowanghan.atlantis.framework.auth.annotation.AuthCacheConstant;
import com.oowanghan.atlantis.framework.auth.entity.AtlantisJwtClaim;
import com.oowanghan.atlantis.framework.common.log.LogPrefixConstant;
import io.jsonwebtoken.Claims;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class JwtTokenCacheProcesser {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenCacheProcesser.class);
    @Autowired
    private RedissonClient redissonClient;

    /**
     * 校验token白名单
     *
     * @param claims
     * @param authToken
     * @return
     */
    public Claims tokenWhiteList(Claims claims, String authToken) {
        log.debug("Token White List Check");
        if (null == claims) {
            return null;
        }
        Object uidObj = claims.get(AtlantisJwtClaim.USER_ID_KEY);
        if (null == uidObj) {
            return null;
        }
        // 白名单模式，Redis中token为有效token
        String cacheToken = getCacheToken(authToken, uidObj.toString());
        // 传过来的token不等于Redis中的token为非法token
        if (!StrUtil.equals(authToken, cacheToken)) {
            log.debug("JWT Token Not Equals Redis Token");
            return null;
        }
        claims.put(AtlantisJwtClaim.AUTHORIZATION, "Bearer " + authToken);
        return claims;
    }

    private String getCacheToken(String authToken, String uid) {
        String md5Token = SecureUtil.md5(authToken);
        RBucket<String> cache = redissonClient.getBucket(AuthCacheConstant.REDIS_KEY_JWT_TOKEN_PRFIX_APP + uid + ":" + md5Token);
        String cacheToken = cache.get();
        return cacheToken;
    }

    /**
     * 登录token写入白名单
     *
     * @param
     * @param jwt
     */
    public void loginTokenWhitelist(Long uid, String jwt, long time) {
        String md5Token = SecureUtil.md5(jwt);
        RBucket<String> cathe;
        cathe = redissonClient.getBucket(AuthCacheConstant.REDIS_KEY_JWT_TOKEN_PRFIX_APP + uid + ":" + md5Token);

        if (time > 0) {
            cathe.set(jwt, time, TimeUnit.MILLISECONDS);
        } else {
            cathe.set(jwt);
        }
    }

    public void logout(Long uid, String jwt) {
        String md5Token = SecureUtil.md5(jwt);
        log.debug(LogPrefixConstant.AUTH + "User : {} logout Token delete White List", uid);
        RBucket<String> cathe;
        cathe = redissonClient.getBucket(AuthCacheConstant.REDIS_KEY_JWT_TOKEN_PRFIX_APP + uid + ":" + md5Token);
        cathe.delete();
    }

}
