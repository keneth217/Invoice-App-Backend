package com.app.invoice.configs.tenants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private SchoolInterceptor schoolInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*") // Allows any origin pattern, but compliant with allowCredentials(true)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("Content-Type", "X-Tenant", "Accept", "Authorization");
//              .allowCredentials(true); // Allow credentials
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(schoolInterceptor);
    }
}
