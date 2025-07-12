package com.app.invoice.configs.tenants;


import com.app.invoice.exception.NoBusinessIdentifierException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Enumeration;

@Component
public class BusinessInterceptor implements HandlerInterceptor {

    private static final String[] EXCLUDED_ENDPOINTS = {
            "/error",
            "/api/online/test-connection",
            
            // Swagger UI and API Documentation
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/api-docs/**",
            "/api-docs",
            "/v3/api-docs/**",
            "/v3/api-docs",
            "/swagger-resources/**",
            "/webjars/**",
            
            // Authentication endpoints
            "/api/auth/**",
            "/api/admin/auth/**",
            
            // Test and health check endpoints
            "/api/test/**",
            "/actuator/**",
            "/health",
            
            // Legacy endpoints (keeping for compatibility)
            "/api/students/active",
            "/api/school/settings/name",
            "/api/admin/users/forgot_password",
            "/api/admin/users/reset_password",
            "/api/users/forgot_password",
            "/api/users/reset_password"
    };

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String requestURI = request.getRequestURI();

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            System.out.println("IS OPTIONS ");
            return true;
        }

        if (isExcludedEndpoint(requestURI)) {
            return true;
        }

        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
        }


        String tenantId = request.getHeader("X-Tenant");
        if (tenantId != null) {
            TenantContext.setCurrentTenant(tenantId);
        } else {
            throw new NoBusinessIdentifierException("No school identifier provided");
        }
        return true;
    }

    // Helper method to check if the request URI is excluded
    private boolean isExcludedEndpoint(String requestURI) {
        for (String excludedEndpoint : EXCLUDED_ENDPOINTS) {
            if (requestURI.matches(excludedEndpoint.replace("**", ".*"))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        TenantContext.clear();
        DatabaseContext.clear();
    }
}
