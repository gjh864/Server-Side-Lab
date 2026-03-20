package com.gonjunhan.helloserver.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import java.io.PrintWriter;

public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 获取本次请求的 HTTP 动词和具体路径
        String method = request.getMethod();
        String uri = request.getRequestURI();

        // 2. 手写细粒度放行规则
        // 规则 A：POST /api/users → 允许注册（公开接口）
        boolean isCreateUser = "POST".equalsIgnoreCase(method) && "/api/users".equals(uri);
        // 规则 B：GET /api/users/* → 允许查看用户信息（公开接口）
        boolean isGetUser = "GET".equalsIgnoreCase(method) && uri.startsWith("/api/users/");

        // 满足任一公开规则，直接放行
        if (isCreateUser || isGetUser) {
            return true;
        }

        // 3. 敏感操作（DELETE/PUT 等）必须校验 Token
        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            PrintWriter writer = response.getWriter();
            String errorJson = "{\"code\":401,\"msg\":\"非法操作：敏感动作 [" + method + "] 需要鉴权\",\"data\":null}";
            writer.write(errorJson);
            writer.flush();
            writer.close();
            return false;
        }

        // Token 存在，放行
        return true;
    }
}