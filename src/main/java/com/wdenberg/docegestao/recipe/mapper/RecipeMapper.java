package com.wdenberg.docegestao.recipe.mapper;


import com.wdenberg.docegestao.recipe.dto.RecipeItemResponse;
import com.wdenberg.docegestao.recipe.dto.RecipeResponse;
import com.wdenberg.docegestao.recipe.entity.Recipe;
import com.wdenberg.docegestao.recipe.entity.RecipeItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RecipeMapper {

    @Mapping(target = "items", source = "items")
    RecipeResponse toReponse(Recipe recipe);

    @Mapping(target = "ingredientId", source = "ingredient.id")
    @Mapping(target = "ingredientName", source = "ingredient.name")
    RecipeItemResponse toItemResponse(RecipeItem item);
}
