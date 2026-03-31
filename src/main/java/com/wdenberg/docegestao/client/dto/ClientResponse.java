package com.wdenberg.docegestao.client.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record ClientResponse(

        UUID id,
        String name,
        String phone,
        String email,
        String address,
        String notes,
        Integer totalOrders,
        BigDecimal totalSpent,
        OffsetDateTime lastOrderAt,
        Boolean active,
        OffsetDateTime createdAr,
        OffsetDateTime updatedAt

) { }
