package com.wdenberg.docegestao.recipe.service;


import com.wdenberg.docegestao.recipe.entity.Recipe;
import com.wdenberg.docegestao.recipe.entity.RecipeItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class RecipePricingService {


    public void recalculate(Recipe recipe){
        BigDecimal ingredientsCost = recipe.getItems().stream()
                .map(item -> item.getCalculatedCost() == null ? BigDecimal.ZERO : item.getCalculatedCost())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal productionCost = ingredientsCost
                .add(nvl(recipe.getLaborCost()))
                .add(nvl(recipe.getEnergyCost()))
                .add(nvl(recipe.getPackagingCost()))
                .add(nvl(recipe.getTransportCost()));

        BigDecimal marginValue = productionCost
                .multiply(nvl(recipe.getProfitMargin()))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

    private BigDecimal nvl(BigDecimal value){
        return  value == null ? BigDecimal.ZERO : value;
    }
}
