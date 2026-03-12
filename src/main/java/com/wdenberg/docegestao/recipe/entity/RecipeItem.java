package com.wdenberg.docegestao.recipe.entity;

import com.wdenberg.docegestao.common.config.BaseEntity;
import com.wdenberg.docegestao.ingredient.entity.Ingredient;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@Entity
@Table(name = "recipe_items")
public class RecipeItem extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Ingredient ingredient;

    @Column(name = "quantity_used", nullable = false, precision = 12, scale = 3)
    private BigDecimal quantityUsed;

    @Column(name = "unit_measure", nullable = false, length = 30)
    private String unitMeasure;

    @Column(name = "calculated_cost", nullable = false, precision = 12, scale = 2)
    private BigDecimal calculatedCost = BigDecimal.ZERO;
}
