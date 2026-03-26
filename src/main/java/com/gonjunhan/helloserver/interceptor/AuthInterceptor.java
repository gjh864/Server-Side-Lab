package com.gonjunhan.helloserver.interceptor;

import com.gonjunhan.helloserver.common.JwtUtil;
import com.gonjunhan.helloserver.common.Result;
import com.gonjunhan.helloserver.common.ResultCode;
import com.google.gson.Gson;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 放行 OPTIONS 预检请求
        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        }

        // 2. 获取 Authorization Header
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // 没有 Token 或格式错误 → 返回 401
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            Result<String> result = Result.error(ResultCode.UNAUTHORIZED);
            response.getWriter().write(new Gson().toJson(result));
            return false;
        }

        // 3. 提取 Token（去掉 "Bearer " 前缀，长度 7）
        String token = authHeader.substring(7);

        try {
            // 4. 验证 Token
            JwtUtil.validateTokenAndGetUsername(token);
            return true; // Token 有效 → 放行
        } catch (RuntimeException e) {
            // Token 无效/过期 → 返回 401
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            Result<String> result = Result.error(ResultCode.UNAUTHORIZED);
            response.getWriter().write(new Gson().toJson(result));
            return false;
        }
    }
}