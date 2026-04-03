package com.wdenberg.docegestao.auth.dto;

public record TokenResponse(
        String accessToken,
        String refreshToken,
        String tokenType,
        Long expiresIn
) {
}
