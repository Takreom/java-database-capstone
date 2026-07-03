package com.project.back_end.services;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@org.springframework.stereotype.Service
public class Service {

    private static final Set<String> SUPPORTED_ROLES = Set.of("admin", "doctor", "patient");

    public Map<String, String> validateToken(String token, String role) {
        Map<String, String> errors = new HashMap<>();

        if (token == null || token.isBlank()) {
            errors.put("token", "Token is required");
        }

        if (role == null || !SUPPORTED_ROLES.contains(role)) {
            errors.put("role", "Unsupported role");
        }

        return errors;
    }
}
