package com.gonjunhan.helloserver.common;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtil {
    // 生成安全的 HS256 密钥（Spring Boot 3.x 推荐写法）
    private static final SecretKey SECRET_KEY =
            Keys.hmacShaKeyFor("my-secret-key-keep-it-safe-1234567890".getBytes());

    // Token 过期时间：1小时（3600000毫秒）
    private static final long EXPIRATION_TIME = 3600000;

    // 生成 Token（传入用户名）
    public static String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .subject(username)  // 替换过时的 setSubject
                .issuedAt(now)
                .expiration(expiryDate) // 替换过时的 setExpiration
                .signWith(SECRET_KEY) // 自动识别算法
                .compact();
    }

    // 验证 Token 并获取用户名
    public static String validateTokenAndGetUsername(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(SECRET_KEY) // 替换过时的 setSigningKey
                    .build()
                    .parseSignedClaims(token) // 替换 parseClaimsJws
                    .getPayload()
                    .getSubject();
        } catch (JwtException e) {
            // Token 无效/过期/篡改时抛出异常
            throw new RuntimeException("Token 无效或已过期");
        }
    }
}