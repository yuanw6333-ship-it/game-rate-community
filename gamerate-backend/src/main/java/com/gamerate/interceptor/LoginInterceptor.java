package com.gamerate.interceptor;

import com.gamerate.common.constant.JwtConstants;
import com.gamerate.common.exception.BusinessException;
import com.gamerate.common.result.ResultCode;
import com.gamerate.utils.JwtUtils;
import com.gamerate.utils.UserContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class LoginInterceptor implements HandlerInterceptor {

    private final JwtUtils jwtUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String authorization = request.getHeader(JwtConstants.AUTHORIZATION_HEADER);
        if (!StringUtils.hasText(authorization) || !authorization.startsWith(JwtConstants.TOKEN_PREFIX)) {
            throw new BusinessException(ResultCode.UNAUTHORIZED.getCode(), "Missing token");
        }

        String token = authorization.substring(JwtConstants.TOKEN_PREFIX.length());
        Long userId = jwtUtils.getUserId(token);
        String username = jwtUtils.getUsername(token);
        UserContext.set(userId, username);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserContext.clear();
    }
}
