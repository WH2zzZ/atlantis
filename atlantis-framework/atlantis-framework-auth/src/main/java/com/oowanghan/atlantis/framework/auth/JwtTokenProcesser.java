package com.oowanghan.atlantis.framework.auth;


import com.oowanghan.atlantis.framework.auth.entity.AtlantisJwtClaim;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;


@Component
public class JwtTokenProcesser {

    private final Logger log = LoggerFactory.getLogger(JwtTokenProcesser.class);

    private static final String AUTHORITIES_KEY = "auth";

    private Key key;

    private Key webKey;

    @Autowired
    private JwtTokenCacheProcesser cacheProcesser;

    @Value("${security.jwt.base64-secret}")
    private String jwtBase64Secret;
    /**
     * 过期时间 秒级
     */
    @Value("${security.jwt.token.exp-seconds}")
    private long tokenValidityInSeconds;
    /**
     * 记住的过期时间 秒级
     */
    @Value("${security.jwt.token.exp-seconds.remember}")
    private long tokenValidityInSecondsForRememberMe;


    private long tokenValidityInMilliseconds;

    private long tokenValidityInMillisecondsForRememberMe;

    @PostConstruct
    public void init() {
        byte[] keyBytes;
        keyBytes = Decoders.BASE64.decode(jwtBase64Secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.tokenValidityInMilliseconds =
                1000 * tokenValidityInSeconds;
        this.tokenValidityInMillisecondsForRememberMe =
                1000 * tokenValidityInSecondsForRememberMe;
    }

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

        cacheProcesser.loginTokenWhitelist(uid, token, validity.getTime());
        return token;
    }

    public AtlantisJwtClaim validateToken(String authToken) {

        try {
            Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(authToken).getBody();
            claims = cacheProcesser.tokenWhiteList(claims, authToken);
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

    public void setAuthentication(String token, Claims claims) {

        if(SecurityContextHolder.getContext().getAuthentication() != null){
            return;
        }

        Collection<? extends GrantedAuthority> authorities = new ArrayList<>();
        Object authObj = claims.get(AUTHORITIES_KEY);

        if(authObj != null) {
            String  auth = authObj.toString();

            if(!StringUtils.isEmpty(auth)) {
                authorities = Arrays.stream(auth.split(",")).map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
            }

            User user = new User(claims.getSubject(), "", authorities);
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(user, token, authorities));
        }
    }

}
