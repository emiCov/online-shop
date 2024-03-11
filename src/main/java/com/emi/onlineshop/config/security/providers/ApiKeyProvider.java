package com.emi.onlineshop.config.security.providers;

import com.emi.onlineshop.config.security.authentications.ApiKeyAuthentication;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class ApiKeyProvider implements AuthenticationProvider {

    private final String key;

    public ApiKeyProvider(String key) {
        this.key = key;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        ApiKeyAuthentication auth = (ApiKeyAuthentication) authentication;

        if (key.equals(((ApiKeyAuthentication) authentication).getKey())) {
            auth.setAuthenticated(true);
            return auth;
        }
        throw new BadCredentialsException("Not authenticated");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return ApiKeyAuthentication.class.equals(authentication);
    }
}
