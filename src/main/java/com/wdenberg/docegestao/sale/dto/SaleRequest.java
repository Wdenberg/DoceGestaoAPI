package com.wdenberg.docegestao.sale.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record SaleRequest(

        UUID clientId,
        @NotNull LocalDate saleDate,
        @DecimalMin("0.0")BigDecimal discount,
        String paymentMethod,
        String notes
) { }
