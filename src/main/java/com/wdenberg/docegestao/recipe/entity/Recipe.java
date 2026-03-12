package com.wdenberg.docegestao.recipe.entity;

import com.wdenberg.docegestao.common.config.BaseEntity;
import com.wdenberg.docegestao.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "recipes")
public class Recipe extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "yield_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal yieldAmount = BigDecimal.ONE;

    @Column(name = "yield_unit", nullable = false, length = 30)
    private String yieldUnit = "unidade";

    @Column(length = 100)
    private String category;

    @Column(name = "profit_margin", nullable = false, precision = 12, scale = 2)
    private BigDecimal profitMargin = new BigDecimal("45.00");

    @Column(name = "energy_cost", nullable = false, precision = 12, scale = 2)
    private BigDecimal energyCost = BigDecimal.ZERO;

    @Column(name = "labor_cost", nullable = false, precision = 12, scale = 2)
    private BigDecimal laborCost = BigDecimal.ZERO;

    @Column(name = "packaging_cost", nullable = false, precision = 12, scale = 2)
    private BigDecimal packagingCost = BigDecimal.ZERO;

    @Column(name = "transport_cost", nullable = false, precision = 12, scale = 2)
    private BigDecimal transportCost = BigDecimal.ZERO;

    @Column(name = "ingredients_cost", nullable = false, precision = 12, scale = 2)
    private BigDecimal ingredientsCost = BigDecimal.ZERO;

    @Column(name = "production_cost", nullable = false, precision = 12, scale = 2)
    private BigDecimal productionCost = BigDecimal.ZERO;

    @Column(name = "suggested_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal suggestedPrice = BigDecimal.ZERO;

    @Column(name = "is_active", nullable = false)
    private Boolean active = true;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecipeItem> items = new ArrayList<>();


}
