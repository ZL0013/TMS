package com.xie.web.controller.system;

import com.xie.framework.web.security.JwtTokenProvider;
import com.xie.model.dto.LoginDTO;
import com.xie.model.dto.RefreshTokenDTO;
import com.xie.model.vo.LoginVO;
import com.xie.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "登录接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api")
public class SysLoginController {
    private final LoginService loginService;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${jwt.refreshTokenExpiration}")
    private long refreshTokenExpiration;

    /**
     * 用户登录
     * refreshToken 通过 HttpOnly Cookie 返回，防止 XSS 攻击
     */
    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public LoginVO login(@Valid @RequestBody LoginDTO loginDTO, HttpServletResponse response) {
        LoginVO loginVO = loginService.login(loginDTO);
        
        // 生成 refresh token
        String refreshToken = jwtTokenProvider.createRefreshToken(loginVO.username());
        
        // 通过 HttpOnly Cookie 返回 refresh token
        setRefreshTokenCookie(response, refreshToken);
        
        return loginVO;
    }

    /**
     * 刷新访问令牌
     * 从 Cookie 中读取 refreshToken
     */
    @Operation(summary = "刷新访问令牌")
    @PostMapping("/refresh-token")
    public LoginVO refreshToken(HttpServletRequest request, HttpServletResponse response) {
        // 从 Cookie 中获取 refresh token
        String refreshToken = getRefreshTokenFromCookie(request);
        
        // 调用 Service 刷新
        LoginVO loginVO = loginService.refreshToken(new RefreshTokenDTO(refreshToken));
        
        // 生成新的 refresh token
        String newRefreshToken = jwtTokenProvider.createRefreshToken(loginVO.username());
        
        // 通过 HttpOnly Cookie 返回新的 refresh token
        setRefreshTokenCookie(response, newRefreshToken);
        
        return loginVO;
    }

    /**
     * 用户登出
     * 清除 HttpOnly Cookie 中的 refresh token
     */
    @Operation(summary = "用户登出")
    @PostMapping("/logout")
    public void logout(HttpServletResponse response) {
        // 清除 refresh token cookie
        clearRefreshTokenCookie(response);
    }

    /**
     * 设置 refresh token 到 HttpOnly Cookie
     */
    private void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);  // 防止 JavaScript 访问
        cookie.setSecure(false);   // 生产环境应设置为 true（仅 HTTPS）
        cookie.setPath("/");       // Cookie 的作用路径
        cookie.setMaxAge((int) (refreshTokenExpiration / 1000)); // 过期时间（秒）
        // cookie.setAttribute("SameSite", "Strict"); // 防止 CSRF（Spring Boot 2.6+ 支持）
        response.addCookie(cookie);
    }

    /**
     * 清除 refresh token Cookie
     */
    private void clearRefreshTokenCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0); // 立即过期
        response.addCookie(cookie);
    }

    /**
     * 从 Cookie 中获取 refresh token
     */
    private String getRefreshTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        throw new RuntimeException("Refresh token 不存在");
    }
}
