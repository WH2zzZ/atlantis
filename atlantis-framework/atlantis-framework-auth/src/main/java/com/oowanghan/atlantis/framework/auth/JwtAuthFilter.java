package com.oowanghan.atlantis.framework.auth;

import cn.hutool.core.util.StrUtil;
import com.oowanghan.atlantis.framework.auth.context.AuthContextHolder;
import com.oowanghan.atlantis.framework.auth.entity.AtlantisJwtClaim;
import io.jsonwebtoken.Claims;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Jwt过滤器，解析jwt的token信息
 *
 * @author wanghan
 */
public class JwtAuthFilter extends GenericFilterBean {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    private final JwtTokenProcesser jwtTokenProcesser;

    public JwtAuthFilter(JwtTokenProcesser jwtTokenProcesser) {
        this.jwtTokenProcesser = jwtTokenProcesser;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;

        String requestURI = request.getRequestURI();

        String jwt = resolveToken(request);

        if (StrUtil.isEmpty(jwt)) {
            AtlantisJwtClaim claims;
            claims = jwtTokenProcesser.validateToken(jwt);
            if (claims != null) {
                jwtTokenProcesser.setAuthentication(jwt, claims);
            }
            AuthContextHolder.getContext().setClaims(claims);
        } else {
            AuthContextHolder.getContext().setClaims(null);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }


    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
