package com.wdenberg.docegestao.security.service;

import java.util.UUID;

public record AuthenticatedUser(
        UUID id,
        String email
) {
}
