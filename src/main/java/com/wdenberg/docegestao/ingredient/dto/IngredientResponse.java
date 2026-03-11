package com.wdenberg.docegestao.ingredient.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record IngredientResponse(

        UUID id,
        String name,
        BigDecimal quantity,
        String unitMeasure,
        BigDecimal purchesePrice,
        BigDecimal costPerUnit,
        String supplier,
        boolean active,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt

) {
}
