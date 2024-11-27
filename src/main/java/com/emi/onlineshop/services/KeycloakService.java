package com.emi.onlineshop.services;

import com.emi.onlineshop.dtos.AuthenticationRequest;
import com.emi.onlineshop.dtos.KeycloakJwtResponse;
import com.emi.onlineshop.dtos.KeycloakRole;
import com.emi.onlineshop.dtos.KeycloakUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class KeycloakService {

    @Value(("${keycloak.clientId}"))
    private String clientId;

    @Value(("${keycloak.clientSecret}"))
    private String clientSecret;

    @Value(("${keycloak.tokenUri}"))
    private String tokenUri;

    @Value(("${keycloak.userUri}"))
    private String userUri;

    @Value(("${keycloak.roleUri}"))
    private String roleUri;

    private static final String ROLES_MAPPING_URI = "/%s/role-mappings/realm";
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER_TOKEN = "Bearer %s";
    private static final String EXCEPTION_THROWN = "Exception thrown";

    private final RestClient keycloakClient;

    public KeycloakService(RestClient keycloakClient) {
        this.keycloakClient = keycloakClient;
    }

    public KeycloakJwtResponse authenticateClient() {
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("grant_type", "client_credentials");

        return keycloakClient
                .post()
                .uri(uriBuilder -> uriBuilder.path(tokenUri).build())
                .body(params)
                .accept(MediaType.APPLICATION_FORM_URLENCODED)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, resp) -> {
                    throw new IllegalArgumentException(EXCEPTION_THROWN);
                })
                .body(KeycloakJwtResponse.class);
    }

    public KeycloakJwtResponse authenticateUser(AuthenticationRequest request) {
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("grant_type", "password");
        params.add("username", request.email());
        params.add("password", request.password());

        return keycloakClient
                .post()
                .uri(uriBuilder -> uriBuilder.path(tokenUri).build())
                .body(params)
                .accept(MediaType.APPLICATION_FORM_URLENCODED)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, response) -> {
                    throw new IllegalArgumentException(EXCEPTION_THROWN);
                })
                .body(KeycloakJwtResponse.class);
    }

    public List<KeycloakUser> getKeyCloakUsers() {
        return keycloakClient
                .get()
                .uri(uriBuilder -> uriBuilder.path(userUri).build())
                .header(AUTHORIZATION, BEARER_TOKEN.formatted(authenticateClient().getAccessToken()))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, response) -> {
                    throw new IllegalArgumentException(EXCEPTION_THROWN);
                })
                .body(new ParameterizedTypeReference<>() {
                });
    }

    private List<KeycloakUser> getKeycloakUserByUsername(String username) {
        return Objects.requireNonNull(keycloakClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(userUri)
                        .queryParam("username", username)
                        .queryParam("exact", "true")
                        .build())
                .header(AUTHORIZATION, BEARER_TOKEN.formatted(authenticateClient().getAccessToken()))
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, response) -> {
                    throw new IllegalArgumentException(EXCEPTION_THROWN);
                })
                .body(new ParameterizedTypeReference<>() {
                }));
    }

    public String createKeycloakUser(KeycloakUser keycloakUser) {
        Optional<KeycloakRole> userRole = getRealmRoleByName(keycloakUser.realmRoles().get(0));
        if (userRole.isEmpty()) {
            throw new IllegalArgumentException("Role: " + keycloakUser.realmRoles().get(0) + " does not exist");
        }

        keycloakClient
                .post()
                .uri(uriBuilder -> uriBuilder.path(userUri).build())
                .body(keycloakUser)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, BEARER_TOKEN.formatted(authenticateClient().getAccessToken()))
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, response) -> {
                    throw new IllegalArgumentException("Can't save user");
                })
                .toBodilessEntity();

        List<KeycloakUser> keycloakUsers = getKeycloakUserByUsername(keycloakUser.username());

        if (keycloakUsers.isEmpty()) {
            throw new IllegalArgumentException("Keycloak user was not saved");
        }

        String keycloakUserId = keycloakUsers.get(0).id();

        keycloakClient
                .post()
                .uri(uriBuilder -> uriBuilder.path(userUri + ROLES_MAPPING_URI.formatted(keycloakUserId)).build())
                .body(List.of(userRole))
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, BEARER_TOKEN.formatted(authenticateClient().getAccessToken()))
                .retrieve()
                .toBodilessEntity();

        return keycloakUserId;
    }

    private List<KeycloakRole> getRealmRoles() {
        List<KeycloakRole> realmRoles = keycloakClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(roleUri)
                        .build())
                .header(AUTHORIZATION, BEARER_TOKEN.formatted(authenticateClient().getAccessToken()))
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        return realmRoles == null ? Collections.emptyList() : realmRoles;
    }

    private Optional<KeycloakRole> getRealmRoleByName(String name) {
        return getRealmRoles().stream().filter(realmRole -> realmRole.name().equals(name)).findFirst();
    }
}
