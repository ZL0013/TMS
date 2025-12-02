package com.xie.framework.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    // TODO: 添加安全配置
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize ->
                authorize.requestMatchers("/doc.html", "/swagger-ui/**", "/swagger-ui.html", "/v3/**", "/test/**", "/webjars/**", "/swagger-resources/**").permitAll());
        http.formLogin(withDefaults());
        http.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()));
        http.csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

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
}
