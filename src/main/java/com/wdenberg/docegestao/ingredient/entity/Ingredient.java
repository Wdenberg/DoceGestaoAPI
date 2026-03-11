package com.wdenberg.docegestao.ingredient.entity;

import com.wdenberg.docegestao.common.config.BaseEntity;
import com.wdenberg.docegestao.user.entity.User;
import jakarta.persistence.*;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "ingredients")

public class Ingredient extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, precision = 12, scale = 3)
    private BigDecimal quantity;

    @Column(name = "unit_measure", nullable = false, length = 30)
    private String unitMeasure;

    @Column(name = "purchase_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal purchasePrice;

    @Column(name = "cost_per_unit", nullable = false, precision = 12, scale = 6)
    private BigDecimal costPerUnit;

    @Column(length = 255)
    private String supplier;

    @Column(nullable = false)
    private Boolean active = true;


}
