package com.gamerate.config;

import com.gamerate.interceptor.LoginInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns(
                        "/api/user/profile",
                        "/api/user/profile/**",
                        "/api/ratings",
                        "/api/ratings/**",
                        "/api/comments",
                        "/api/comments/**",
                        "/api/favorites",
                        "/api/favorites/**",
                        "/api/admin/**"
                )
                .excludePathPatterns(
                        "/api/health",
                        "/api/user/register",
                        "/api/user/login",
                        "/doc.html",
                        "/webjars/**",
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-resources/**"
                );
    }
}
