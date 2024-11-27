package com.emi.onlineshop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

//@Configuration
public class WebClientConfig {

    @Value(("${keycloak.baseUrl}"))
    private String baseUrl;

    @Bean
    RestClient keycloakClient() {
        return RestClient.builder().baseUrl(baseUrl).build();
    }
}
