package com.wdenberg.docegestao.user.entity;

public enum RoleName {

    ROLE_ADMIN("Administrador"),
    ROLE_USER("Usuário");

    private final String description;

    RoleName(String description) {
        this.description = description;
    }
}
