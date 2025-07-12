package com.app.invoice.security.jwt;



import com.app.invoice.configs.tenants.DatabaseContext;
import com.app.invoice.configs.tenants.TenantContext;
import com.app.invoice.exception.BusinessDeactivatedException;
import com.app.invoice.exception.UsernameNotFoundException;
import com.app.invoice.master.entity.Business;
import com.app.invoice.master.services.BusinessService;
import com.app.invoice.security.services.UserDetailsServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;


public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private BusinessService pharmacyService;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String tenantId = request.getHeader("X-TenantID");
            if (tenantId != null) {
                if ("master".equalsIgnoreCase(tenantId)) {
                    DatabaseContext.setCurrentDatabase("master");
                } else {
                    TenantContext.setCurrentTenant(tenantId);
                    Business school = pharmacyService.findByBusinessId(UUID.fromString(tenantId));
                    if (school == null) {
                        throw new UsernameNotFoundException("School not found with ID: " + tenantId);
                    }
                    if (Boolean.FALSE.equals(school.getActivated())) {
                        throw new BusinessDeactivatedException("School is deactivated. Contact admin.");
                    }
                }
            }

            String jwt = parseJwt(request);
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String phoneNo = jwtUtils.getUserNameFromJwtToken(jwt);

                UserDetails userDetails = userDetailsService.loadUserByUsername(phoneNo);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } catch (BusinessDeactivatedException | ExpiredJwtException e) {
            throw e;
        } catch (Exception e) {
            throw new ServletException("Authentication failed", e);
        } finally {
            TenantContext.clear();
            DatabaseContext.clear();
        }

        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }
}