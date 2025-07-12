package com.app.invoice.configs.tenants;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.lang.NonNull;

import java.io.IOException;

public class PreAuthFilter extends OncePerRequestFilter {
    
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
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        
        // Skip tenant validation for excluded endpoints
        if (isExcludedEndpoint(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        String tenantId = request.getHeader("X-Tenant");

        if (tenantId == null || tenantId.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing X-Tenant header");
            return;
        }

        try {
            TenantContext.setCurrentTenant(tenantId);
            filterChain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
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
}
