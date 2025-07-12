//package com.samis.analytics.security.services;
//
//import io.github.bucket4j.Bandwidth;
//import io.github.bucket4j.Bucket;
//import io.github.bucket4j.Bucket4j;
//import io.github.bucket4j.Refill;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.time.Duration;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//@Component
//public class RateLimitingFilter extends OncePerRequestFilter {
//
//    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();
//
//    private Bucket resolveBucket(String ip) {
//        return buckets.computeIfAbsent(ip, k -> createNewBucket());
//    }
//
//    private Bucket createNewBucket() {
//        Bandwidth limit = Bandwidth.classic(10, Refill.intervally(60, Duration.ofMinutes(1)));
//        return Bucket4j.builder().addLimit(limit).build();
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain) throws ServletException, IOException {
//
//        String ip = request.getRemoteAddr();
//        Bucket bucket = resolveBucket(ip);
//
//        if (bucket.tryConsume(1)) {
//            filterChain.doFilter(request, response);
//        } else {
//            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
//            response.getWriter().write("Too many requests - try again later");
//        }
//    }
//}
