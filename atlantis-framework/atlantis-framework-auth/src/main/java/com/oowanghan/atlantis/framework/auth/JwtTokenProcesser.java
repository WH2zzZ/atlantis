package com.oowanghan.atlantis.framework.auth;


import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.oowanghan.atlantis.framework.auth.annotation.AuthCacheConstant;
import com.oowanghan.atlantis.framework.auth.entity.AtlantisJwtClaim;
import com.oowanghan.atlantis.framework.auth.properties.JwtProperties;
import com.oowanghan.atlantis.framework.common.log.LogPrefixConstant;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.concurrent.TimeUnit;


@Component
public class JwtTokenProcesser {

    private final Logger log = LoggerFactory.getLogger(JwtTokenProcesser.class);

    private static final String AUTHORITIES_KEY = "auth";

    private final JwtProperties jwtProperties;
    private final RedissonClient redissonClient;

    public JwtTokenProcesser(JwtProperties jwtProperties,
                             RedissonClient redissonClient) {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getBase64Secret());
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.tokenValidityInMilliseconds =
                1000 * jwtProperties.getTokenExpInSeconds();
        this.tokenValidityInMillisecondsForRememberMe =
                1000 * jwtProperties.getTokenExpInSecondsRememberMe();
        this.redissonClient = redissonClient;
        this.jwtProperties = jwtProperties;
    }

    private final Key key;
    private final long tokenValidityInMilliseconds;
    private final long tokenValidityInMillisecondsForRememberMe;

    public String createToken(Long uid, String mobile, String username, boolean rememberMe) {
        long now = (new Date()).getTime();
        Date validity;
        if (rememberMe) {
            validity = new Date(now + this.tokenValidityInMillisecondsForRememberMe);
        } else {
            validity = new Date(now + this.tokenValidityInMilliseconds);
        }
        String token = Jwts.builder()
                .setSubject("WEB")
                .claim("uid", uid)
                .claim("user", username)
                .claim("mobile", mobile)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();

        writeWhitelist(uid, token, validity.getTime());
        return token;
    }

    public AtlantisJwtClaim validateToken(String authToken) {

        try {
            Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(authToken).getBody();
            claims = isWhitelist(claims, authToken);
            return new AtlantisJwtClaim(claims);
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT signature.");
            log.debug("Invalid JWT signature trace: {}", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
            log.debug("Expired JWT token trace: {}", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
            log.debug("Unsupported JWT token trace: {}", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
            log.debug("JWT token compact of handler are invalid trace: {}", e);
        }
        return null;
    }

    /**
     * 校验token白名单
     *
     * @param claims
     * @param authToken
     * @return
     */
    public Claims isWhitelist(Claims claims, String authToken) {
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
    public void writeWhitelist(Long uid, String jwt, long time) {
        String md5Token = SecureUtil.md5(jwt);
        RBucket<String> cathe;
        cathe = redissonClient.getBucket(AuthCacheConstant.REDIS_KEY_JWT_TOKEN_PRFIX_APP + uid + ":" + md5Token);

        if (time > 0) {
            cathe.set(jwt, time, TimeUnit.MILLISECONDS);
        } else {
            cathe.set(jwt);
        }
    }

    public void deleteToken(Long uid, String jwt) {
        String md5Token = SecureUtil.md5(jwt);
        log.debug(LogPrefixConstant.AUTH + "User : {} logout Token delete White List", uid);
        RBucket<String> cathe;
        cathe = redissonClient.getBucket(AuthCacheConstant.REDIS_KEY_JWT_TOKEN_PRFIX_APP + uid + ":" + md5Token);
        cathe.delete();
    }
}
