package com.xie.framework.web.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

/**
 * Spring Security 配置
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity  // 启用方法级别的安全注解（@PreAuthorize, @PostAuthorize）
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationEntryPointImpl authenticationEntryPoint;
    private final AccessDeniedHandlerImpl accessDeniedHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;


    /**
     * 安全过滤器链配置
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 请求授权配置
        http.authorizeHttpRequests(authorize ->
                authorize
                    // 放行 Knife4j 文档和 API
                    .requestMatchers(
                        "/doc.html",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/webjars/**",
                        "/favicon.ico",
                        "/**/favicon.ico"
                    ).permitAll()
                    // 放行测试接口
                    .requestMatchers("/test/**").permitAll()
                    // 放行登录和刷新令牌接口
                    .requestMatchers("/v1/api/login", "/v1/api/refresh-token").permitAll()
                    // 其他请求需要认证
                    .anyRequest().authenticated()
        );
        
        // 禁用表单登录（使用 JWT）
        http.formLogin(AbstractHttpConfigurer::disable);
        
        // 配置 CORS
        http.cors(httpSecurityCorsConfigurer -> 
            httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()));
        
        // 禁用 CSRF（使用 JWT 不需要 CSRF 保护）
        http.csrf(AbstractHttpConfigurer::disable);
        
        // 配置 session 管理：无状态
        http.sessionManagement(session -> 
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );
        
        // 添加 JWT 过滤器
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        // 配置异常处理
        http.exceptionHandling(exception ->
            exception
                .authenticationEntryPoint(authenticationEntryPoint)  // 401 认证失败
                .accessDeniedHandler(accessDeniedHandler)            // 403 权限不足
        );
        
        return http.build();
    }

    /**
     * CORS 配置
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        return request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedOrigins(List.of("*"));
            config.setAllowedMethods(List.of("*"));
            config.setAllowedHeaders(List.of("*"));
            config.setAllowCredentials(false);
            return config;
        };
    }

    /**
     * 密码编码器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
