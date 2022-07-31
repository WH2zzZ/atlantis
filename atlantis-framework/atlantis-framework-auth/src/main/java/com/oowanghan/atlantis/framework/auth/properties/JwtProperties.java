package com.oowanghan.atlantis.framework.auth.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author WangHan
 * @Create 2022/7/31 17:32
 */
@ConfigurationProperties(prefix = "security.jwt", ignoreUnknownFields = false)
public class JwtProperties {

    private String base64Secret;
    /**
     * 过期时间 秒级
     */
    private long tokenExpInSeconds;
    /**
     * 记住的过期时间 秒级
     */
    private long tokenExpInSecondsRememberMe;

    public String getBase64Secret() {
        return base64Secret;
    }

    public void setBase64Secret(String base64Secret) {
        this.base64Secret = base64Secret;
    }

    public long getTokenExpInSeconds() {
        return tokenExpInSeconds;
    }

    public void setTokenExpInSeconds(long tokenExpInSeconds) {
        this.tokenExpInSeconds = tokenExpInSeconds;
    }

    public long getTokenExpInSecondsRememberMe() {
        return tokenExpInSecondsRememberMe;
    }

    public void setTokenExpInSecondsRememberMe(long tokenExpInSecondsRememberMe) {
        this.tokenExpInSecondsRememberMe = tokenExpInSecondsRememberMe;
    }
}
