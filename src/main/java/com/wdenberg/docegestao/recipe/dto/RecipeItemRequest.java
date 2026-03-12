package com.wdenberg.docegestao.recipe.dto;



import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record RecipeItemRequest(
        @NotNull UUID ingredientId,
        @NotNull @DecimalMin("0.001") BigDecimal quantityUsed,
        @NotBlank String unitMeasure
) {}
