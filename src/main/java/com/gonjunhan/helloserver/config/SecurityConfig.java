package com.gonjunhan.helloserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 开启跨域
                .cors(cors -> cors.configure(http))
                // 强制关闭 CSRF
                .csrf(csrf -> csrf.disable())
                // 无状态会话
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // 放行注册、登录
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/users", "/api/users/login").permitAll()
                        // 其他所有请求都需要认证
                        .anyRequest().authenticated()
                )
                // 关闭默认登录页和 HTTP Basic
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable());

        return http.build();
    }
}