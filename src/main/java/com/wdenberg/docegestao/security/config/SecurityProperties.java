package com.wdenberg.docegestao.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security.jwt")
public record SecurityProperties(
        String secret,
        long accesTokenExpirationMinutes
) {
}
