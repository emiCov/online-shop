package com.emi.onlineshop.config.security.filters;


import com.emi.onlineshop.config.security.authentications.ApiKeyAuthentication;
import com.emi.onlineshop.config.security.managers.ApiKeyAuthenticationManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class ApiKeyFilter extends OncePerRequestFilter {

    @Value("${the.key}")
    private final String key;

    public ApiKeyFilter(String key) {
        this.key = key;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        ApiKeyAuthenticationManager manager = new ApiKeyAuthenticationManager(key);

        String requestKey = request.getHeader("x-api-key");

        if ("null".equals(requestKey) || requestKey == null) {
            filterChain.doFilter(request, response);
            return;
        }

        ApiKeyAuthentication auth = new ApiKeyAuthentication(requestKey);

        try {
            Authentication a = manager.authenticate(auth);

            if (a.isAuthenticated()) {
                SecurityContextHolder.getContext().setAuthentication(a);
                filterChain.doFilter(request, response);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }

        } catch (AuthenticationException exception) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
