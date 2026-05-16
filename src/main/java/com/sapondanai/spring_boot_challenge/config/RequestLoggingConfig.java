package com.sapondanai.spring_boot_challenge.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Configuration
public class RequestLoggingConfig extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RequestLoggingConfig.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        filterChain.doFilter(request, response);
        log.info("{} {} -> {}", request.getMethod(), request.getRequestURI(), response.getStatus());
    }
}
