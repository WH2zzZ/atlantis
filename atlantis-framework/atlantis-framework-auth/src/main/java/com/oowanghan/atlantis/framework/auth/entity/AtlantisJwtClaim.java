package com.oowanghan.atlantis.framework.auth.entity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;

import java.util.Map;

/**
 * @Author WangHan
 * @Create 2022/7/28 00:45
 */
public class AtlantisJwtClaim extends DefaultClaims {

    /**
     * 用户名
     */
    public static String USER_KEY = "username";
    /**
     * 用户id
     */
    public static String USER_ID_KEY = "uid";
    /**
     * 用户角色
     */
    public static String ROLES = "roles";

    /**
     * auth 全文
     */
    public static String AUTHORIZATION = "Authorization";

    public AtlantisJwtClaim(Map<String, ?> map) {
        super(map);
    }

    public Claims setUser(String username) {
        this.setValue(USER_KEY, username);
        return this;
    }

    public String getUser() {
        return this.getString(USER_KEY);
    }

    public Claims setUserId(String userId) {
        this.setValue(USER_ID_KEY, userId);
        return this;
    }

    public String getUserId() {
        return this.getString(USER_ID_KEY);
    }

    public Claims setRole(String role) {
        this.setValue(ROLES, role);
        return this;
    }

    public String getRole() {
        return this.getString(ROLES);
    }
}
