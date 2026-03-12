package com.wdenberg.docegestao.recipe.dto;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;


public record RecipeRequest(
        @NotBlank String name,
        String description,
        String imageUrl,
        @NotNull @DecimalMin("0.01") BigDecimal yieldAmount,
        @NotBlank String yieldUnit,
        String category,
        @NotNull @DecimalMin("0.0") BigDecimal profitMargin,
        BigDecimal energyCost,
        BigDecimal laborCost,
        BigDecimal packagingCost,
        BigDecimal transportCost,
        Boolean active
) {

}