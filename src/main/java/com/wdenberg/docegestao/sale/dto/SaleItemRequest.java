package com.wdenberg.docegestao.sale.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record SaleItemRequest(
        @NotNull UUID recipeId,
        @NotNull @DecimalMin("0.01") BigDecimal quantity,
        @NotNull @DecimalMin("0.0") BigDecimal unitPrice
) {}