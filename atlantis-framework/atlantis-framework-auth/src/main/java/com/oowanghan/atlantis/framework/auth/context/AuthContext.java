package com.oowanghan.atlantis.framework.auth.context;

import com.oowanghan.atlantis.framework.auth.entity.AtlantisJwtClaim;
import io.jsonwebtoken.Claims;

public class AuthContext {
    private Long uid;
    private String username;
    private AtlantisJwtClaim claims;
    private String roles;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public Claims getClaims() {
        return claims;
    }

    public void setClaims(AtlantisJwtClaim claims) {
        this.claims = claims;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }
}