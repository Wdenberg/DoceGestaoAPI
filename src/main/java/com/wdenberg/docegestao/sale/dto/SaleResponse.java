package com.wdenberg.docegestao.sale.dto;

import com.wdenberg.docegestao.sale.entity.SaleStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record SaleResponse(

        UUID id,
        UUID clientId,
        String clientName,
        LocalDate saleDate,
        SaleStatus status,
        BigDecimal subTotal,
        BigDecimal discount,
        BigDecimal totalAmount,
        String paymentMethod,
        String notes,
        List<SaleItemResponse> items,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt


) {
}
