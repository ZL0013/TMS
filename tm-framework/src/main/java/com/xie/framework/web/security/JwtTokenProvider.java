package com.xie.framework.web.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT 提供者
 */
@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${jwt.secretKey}")
    private String secretKey;

    /**
     * -- GETTER --
     * 获取访问令牌过期时间（毫秒）
     */
    @Getter
    @Value("${jwt.accessTokenExpiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refreshTokenExpiration}")
    private long refreshTokenExpiration;

    private SecretKey key;

    /**
     * 初始化方法，生成安全的密钥
     * 使用 HMAC-SHA256 算法，密钥长度 >= 256 位
     */
    @PostConstruct
    protected void init() {
        // 使用配置的密钥生成 SecretKey，确保长度符合 HS256 要求（>= 256 位）
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 32) {
            log.warn("JWT 密钥长度不足 256 位，将使用 Jwts.SIG.HS256.key() 生成安全密钥");
            this.key = Jwts.SIG.HS256.key().build();
        } else {
            this.key = Keys.hmacShaKeyFor(keyBytes);
        }
        log.info("JWT 密钥初始化完成，算法: {}", this.key.getAlgorithm());
    }

    /**
     * 创建访问令牌
     *
     * @param username 用户名
     * @return 访问令牌
     */
    public String createAccessToken(String username) {
        // 构建 JWT token，设置主体、签发时间、过期时间和签名
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .signWith(key)
                .compact();
    }

    /**
     * 创建刷新令牌
     *
     * @param username 用户名
     * @return 刷新令牌
     */
    public String createRefreshToken(String username) {
        // 构建刷新令牌，有效期更长，用于刷新访问令牌
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .signWith(key)
                .compact();
    }

    /**
     * 解析 JWT token，提取 Claims
     *
     * @param token JWT token
     * @return Claims 对象
     */
    public Claims parseClaims(String token) {
        // 使用密钥验证签名并解析 token，如果签名无效或已过期会抛出异常
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 从 HTTP 请求中提取 JWT token
     *
     * @param request HTTP 请求对象
     * @return JWT token，如果不存在则返回 null
     */
    public String resolveToken(HttpServletRequest request) {
        // 从 Authorization 请求头获取 Bearer token
        String bearerToken = request.getHeader("Authorization");
        // 检查是否以 "Bearer " 开头
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            // 去掉 "Bearer " 前缀，返回实际的 token
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     * 从 token 中获取用户名
     *
     * @param token JWT token
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        // 解析 token 并返回主体（用户名）
        return parseClaims(token).getSubject();
    }

    /**
     * 验证 JWT token 是否有效
     *
     * @param token JWT token
     * @return 是否有效
     */
    public boolean validateToken(String token) {
        try {
            // 验证签名和过期时间，任何验证失败都会抛出异常
            parseClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("JWT token 已过期: {}", e.getMessage());
            return false;
        } catch (SecurityException | MalformedJwtException e) {
            log.warn("无效的 JWT token: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("JWT token 验证失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 验证 refresh token 是否有效（允许过期）
     *
     * @param token JWT token
     * @return 是否有效
     */
    public boolean validateRefreshToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("Refresh token 已过期: {}", e.getMessage());
            return false;
        } catch (SecurityException | MalformedJwtException e) {
            log.warn("无效的 Refresh token: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("Refresh token 验证失败: {}", e.getMessage());
            return false;
        }
    }

}
