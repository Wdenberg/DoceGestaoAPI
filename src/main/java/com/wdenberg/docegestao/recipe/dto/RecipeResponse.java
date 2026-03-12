package com.wdenberg.docegestao.recipe.dto;


import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record RecipeResponse(
        UUID id,
        String name,
        String description,
        String imageUrl,
        BigDecimal yieldAmount,
        String yieldUnit,
        String category,
        BigDecimal profitMargin,
        BigDecimal energyCost,
        BigDecimal laborCost,
        BigDecimal packagingCost,
        BigDecimal transportCost,
        BigDecimal ingredientsCost,
        BigDecimal productionCost,
        BigDecimal suggestedPrice,
        Boolean active,
        List<RecipeItemResponse> items,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {}