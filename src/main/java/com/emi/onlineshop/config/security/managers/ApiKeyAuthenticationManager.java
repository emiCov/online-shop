package com.emi.onlineshop.config.security.managers;

import com.emi.onlineshop.config.security.providers.ApiKeyProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class ApiKeyAuthenticationManager implements AuthenticationManager {

    private final String key;

    public ApiKeyAuthenticationManager(String key) {
        this.key = key;
    }


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        ApiKeyProvider apiKeyProvider = new ApiKeyProvider(key);

        if (apiKeyProvider.supports(authentication.getClass())) {
            return apiKeyProvider.authenticate(authentication);
        }

        return authentication;
    }
}
