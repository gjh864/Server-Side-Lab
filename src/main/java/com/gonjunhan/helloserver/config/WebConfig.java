package com.gonjunhan.helloserver.config;

import com.gonjunhan.helloserver.interceptor.AuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor())
                .addPathPatterns("/api/users/**") // 拦截所有用户接口
                .excludePathPatterns(
                        "/api/users",        // 放行注册接口
                        "/api/users/login"   // 放行登录接口
                );
    }
}