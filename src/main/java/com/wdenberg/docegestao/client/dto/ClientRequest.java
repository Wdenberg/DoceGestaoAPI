package com.wdenberg.docegestao.client.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ClientRequest(
        @NotBlank @Size(max = 150) String name,
        @Size(max = 30) String phone,
        @Size(max = 120) String email,
        String address,
        String notes,
        Boolean active

) { }
