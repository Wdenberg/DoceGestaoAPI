package com.wdenberg.docegestao.sale.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record SaleItemResponse(
        UUID id,
        UUID recipeId,
        String recipeName,
        BigDecimal quantity,
        BigDecimal unitPrice,
        BigDecimal totalPrice
) { }
