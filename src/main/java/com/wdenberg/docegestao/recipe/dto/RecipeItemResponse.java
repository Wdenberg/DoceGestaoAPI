package com.wdenberg.docegestao.recipe.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record RecipeItemResponse(
        UUID id,
        UUID ingredientId,
        String ingredientName,
        BigDecimal quantityUsed,
        String unitMeasure,
        BigDecimal calculatedCost
) {}
