package com.example.obwiki.config;

import com.example.obwiki.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {

    @Autowired
    LoginInterceptor loginInterceptor;

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/obwiki/test/**",
                        "/obwiki/redis/**",
                        "/obwiki/user/login",
                        "/obwiki/user/logout/**",
                        "/obwiki/user/getSalt/**",
                        "/obwiki/user/getSalt?",
                        "/obwiki/category/all",
                        "/obwiki/ebook/list",
                        "/obwiki/doc/all/**",
                        "/obwiki/doc/vote/**",
                        "/obwiki/content/findContent/**",
                        "/obwiki/ebookSnapshot/**"
                );
    }
}
