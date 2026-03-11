package com.wdenberg.docegestao.ingredient.mapper;

import com.wdenberg.docegestao.ingredient.dto.IngredientResponse;
import com.wdenberg.docegestao.ingredient.entity.Ingredient;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IngredientMapper {

    IngredientResponse toResponse(Ingredient ingredient);
}
