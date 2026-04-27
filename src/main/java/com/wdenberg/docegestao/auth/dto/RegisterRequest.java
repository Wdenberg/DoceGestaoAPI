package com.wdenberg.docegestao.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;


public record RegisterRequest(
        @NotBlank @Size(min = 3, max = 120) String name,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 8, max = 100) String password,
        List<String> roles
) {
    public RegisterRequest(String name, String email, String password) {
        this(name, email, password, java.util.List.of());
    }
}
