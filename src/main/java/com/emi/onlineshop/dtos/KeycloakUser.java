package com.emi.onlineshop.dtos;

import java.util.List;

public record KeycloakUser(
        String id,
        String username,
        String firstName,
        String lastName,
        List<Credentials> credentials,
        String email,
        boolean emailVerified,
        boolean enabled,
        List<String> realmRoles
) {
}
