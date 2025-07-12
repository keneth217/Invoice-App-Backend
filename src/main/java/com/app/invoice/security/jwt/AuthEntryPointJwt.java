package com.app.invoice.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.app.invoice.exception.BusinessDeactivatedException;
import com.app.invoice.exception.UsernameNotFoundException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {

        setSecurityHeaders(response);

        logErrorSafely(request, authException);

        sendSanitizedErrorResponse(response, authException);
    }

    private void setSecurityHeaders(HttpServletResponse response) {
        response.setHeader("X-Content-Type-Options", "nosniff");
        response.setHeader("X-Frame-Options", "DENY");
        response.setHeader("X-XSS-Protection", "1; mode=block");
        response.setHeader("Content-Security-Policy", "default-src 'self'");
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
    }

    private void logErrorSafely(HttpServletRequest request, AuthenticationException authException) {
        String sanitizedPath = HtmlUtils.htmlEscape(request.getRequestURI());
        String sanitizedMessage = HtmlUtils.htmlEscape(authException.getMessage());

        Throwable cause = authException.getCause();
        if (cause instanceof BusinessDeactivatedException) {
            logger.error("Access denied (School deactivated). Path: {}", sanitizedPath);
        } else if (cause instanceof ExpiredJwtException) {
            logger.error("Expired JWT. Path: {}", sanitizedPath);
        } else {
            logger.error("Unauthorized access. Path: {}, Error: {}", sanitizedPath, sanitizedMessage);
        }
    }

    private void sendSanitizedErrorResponse(HttpServletResponse response, AuthenticationException authException)
            throws IOException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, Object> body = new LinkedHashMap<>();
        int status = HttpServletResponse.SC_UNAUTHORIZED;
        body.put("status", status);
        body.put("error", "Unauthorized");

        Throwable cause = authException.getCause();
        if (cause != null) {
            if (cause instanceof BusinessDeactivatedException) {
                status = HttpServletResponse.SC_FORBIDDEN;
                body.put("error", "Forbidden");
                body.put("message", "School account deactivated. Contact support.");
            } else if (cause instanceof ExpiredJwtException) {
                body.put("message", "Session expired. Please log in again.");
            } else if (cause instanceof UsernameNotFoundException) {
                body.put("message", "User not found with provided credentials");
            } else {
                body.put("message", HtmlUtils.htmlEscape(cause.getMessage()));
            }
        }

        else {
            if (authException instanceof BadCredentialsException) {
                body.put("message", "Invalid phone number or password");
            } else if (authException instanceof DisabledException) {
                body.put("message", "Account is disabled");
            } else if (authException instanceof LockedException) {
                body.put("message", "Account is locked");
            } else {
                body.put("message", HtmlUtils.htmlEscape(authException.getMessage()));
            }
        }

        response.setStatus(status);
        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
    }

}