package com.oowanghan.atlantis.framework.auth;

import com.oowanghan.atlantis.framework.auth.annotation.AuthControl;
import com.oowanghan.atlantis.framework.auth.context.AuthContextHolder;
import com.oowanghan.atlantis.framework.auth.entity.AtlantisJwtClaim;
import com.oowanghan.atlantis.framework.common.exception.BizErrorCode;
import com.oowanghan.atlantis.framework.common.exception.BizException;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Optional;

/**
 * 拦截器 对User信息进行解析，并设置到上下文中
 */
@Component
public class JwtAuthInterceptor implements AsyncHandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        Method method = handlerMethod.getMethod();
        if (method.getAnnotation(AuthControl.class) == null) {
            return true;
        }

        Claims claims = AuthContextHolder.getContext().getClaims();
        if (claims != null) {
            Optional.ofNullable(claims.get(AtlantisJwtClaim.USER_ID_KEY))
                    .ifPresent(uid -> AuthContextHolder.getContext().setUid(Long.parseLong(uid.toString())));

            Optional.ofNullable(claims.get(AtlantisJwtClaim.USER_KEY))
                    .ifPresent(user -> AuthContextHolder.getContext().setUsername(user.toString()));

            Optional.ofNullable(claims.get(AtlantisJwtClaim.ROLES))
                    .ifPresent(roles -> AuthContextHolder.getContext().setRoles(roles.toString()));
        } else {
            throw new BizException(BizErrorCode.AUTH_ERROR);
        }
        return true;
    }
}
