package com.wdenberg.docegestao.sale.entity;

import static javax.print.attribute.standard.JobState.COMPLETED;

public enum SaleStatus {
    PENDING("Pendente"),
    CONFIRMED("Confirmado"),
    COMPLETED("Concluído"),
    CANCELED("Cancelado");

    private final String description;

    SaleStatus(String description) {
        this.description = description;
    }
}
