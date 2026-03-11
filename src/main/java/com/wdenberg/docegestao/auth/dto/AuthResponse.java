package com.wdenberg.docegestao.auth.dto;

import lombok.extern.java.Log;

import java.util.Set;

public record AuthResponse(
        String accessToken,
        String tokenType,
        Long expiresIn,
        String name,
        String email,
        Set<String> roles
) {
}
