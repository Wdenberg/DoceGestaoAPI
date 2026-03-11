package com.wdenberg.docegestao.ingredient.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record IngredientRequest(
        @NotBlank @Size(max = 150) String name,
        @NotNull @DecimalMin(value = "0.001")BigDecimal quantity,
        @NotBlank @Size(max = 30) String unitMeasure,
        @NotNull @DecimalMin(value = "0.0") BigDecimal purchasePrice,
        @Size(max = 255) String supplier,
        Boolean active
        ) {
}
