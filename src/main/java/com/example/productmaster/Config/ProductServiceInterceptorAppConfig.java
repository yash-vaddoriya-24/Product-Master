package com.example.productmaster.Config;

import com.example.productmaster.Interceptor.ProductServiceInterceptor;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;

@Configuration
public class ProductServiceInterceptorAppConfig implements WebMvcConfigurer {

    @Autowired
    private ProductServiceInterceptor productServiceinterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(productServiceinterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/index", "/signup","/assets/**"); // Adjust the paths to exclude from interception
    }

    @Bean
    public Filter noCacheFilter() {
        return (request, response, chain) -> {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            httpResponse.setHeader("Pragma", "no-cache");
            httpResponse.setHeader("Expires", "0");
            chain.doFilter(request, response);
        };
    }
}
