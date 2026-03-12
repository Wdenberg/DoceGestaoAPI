package com.wdenberg.docegestao.recipe.service;


import com.wdenberg.docegestao.common.exception.ResouceNotFoundExceptio;
import com.wdenberg.docegestao.ingredient.repository.IngredientRepository;
import com.wdenberg.docegestao.recipe.dto.RecipeItemRequest;
import com.wdenberg.docegestao.recipe.dto.RecipeRequest;
import com.wdenberg.docegestao.recipe.dto.RecipeResponse;
import com.wdenberg.docegestao.recipe.entity.Recipe;
import com.wdenberg.docegestao.recipe.entity.RecipeItem;
import com.wdenberg.docegestao.recipe.mapper.RecipeMapper;
import com.wdenberg.docegestao.recipe.repository.RecipeRepository;
import com.wdenberg.docegestao.recipe.repository.RecipeSpecification;
import com.wdenberg.docegestao.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RecipeService {


    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final IngredientRepository ingredientRepository;
    private final RecipeMapper recipeMapper;
    private final RecipePricingService recipePricingService;


    @Transactional
    public RecipeResponse create(UUID userId, RecipeRequest recipeRequest){
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new ResouceNotFoundExceptio("Usuário não encontrado"));

        Recipe recipe = new Recipe();
        recipe.setUser(user);
        recipe.setName(recipeRequest.name());
        recipe.setDescription(recipeRequest.description());
        recipe.setImageUrl(recipeRequest.imageUrl());
        recipe.setYieldAmount(recipeRequest.yieldAmount());
        recipe.setYieldUnit(recipeRequest.yieldUnit());
        recipe.setCategory(recipeRequest.category());
        recipe.setProfitMargin(recipeRequest.profitMargin());
        recipe.setEnergyCost(nvl(recipeRequest.energyCost()));
        recipe.setLaborCost(nvl(recipeRequest.laborCost()));
        recipe.setPackagingCost(nvl(recipeRequest.packagingCost()));
        recipe.setTransportCost(nvl(recipeRequest.transportCost()));
        recipe.setActive(recipeRequest.active() == null ? Boolean.TRUE : recipeRequest.active());

        recipePricingService.recalculate(recipe);

        return recipeMapper.toReponse(recipeRepository.save(recipe));
    }

    @Transactional
    public RecipeResponse addItem(UUID recipeId, UUID userId, RecipeItemRequest request){

        Recipe recipe = recipeRepository.findByIdAndUser_Id(recipeId, userId)
                .orElseThrow(() -> new ResouceNotFoundExceptio("Receita não encontrada"));



        var ingredient = ingredientRepository.findByIdAndUserId(request.ingredientId(), userId)
                .orElseThrow(() -> new ResouceNotFoundExceptio("Ingrediente não encontrado"));

        RecipeItem item = new RecipeItem();

        item.setRecipe(recipe);
        item.setIngredient(ingredient);
        item.setQuantityUsed(request.quantityUsed());
        item.setUnitMeasure(request.unitMeasure());
        item.setCalculatedCost(
                ingredient.getCostPerUnit()
                        .multiply(request.quantityUsed())
                        .setScale(2, RoundingMode.HALF_UP)
        );

        recipe.getItems().add(item);
        recipePricingService.recalculate(recipe);
        return recipeMapper.toReponse(recipeRepository.save(recipe));

    }

    @Transactional(readOnly = true)
    public Page<RecipeResponse> findAll(UUID userId, String name, Boolean active, String category, Pageable pageable){
        return recipeRepository.findAll(RecipeSpecification.byFilters(userId, name, category, active), pageable)
                .map(recipeMapper::toReponse);
    }

    @Transactional(readOnly = true)
    public RecipeResponse update(UUID id, UUID useId, RecipeRequest request){
        Recipe recipe = recipeRepository.findByIdAndUser_Id(id, useId).orElseThrow(() -> new ResouceNotFoundExceptio("Receita não encontrada"));

        recipe.setName(request.name());
        recipe.setDescription(request.description());
        recipe.setImageUrl(request.imageUrl());
        recipe.setYieldUnit(request.yieldUnit());
        recipe.setYieldAmount(request.yieldAmount());
        recipe.setCategory(request.category());
        recipe.setProfitMargin(request.profitMargin());
        recipe.setEnergyCost(nvl(request.energyCost()));
        recipe.setLaborCost(nvl(request.laborCost()));
        recipe.setPackagingCost(nvl(request.packagingCost()));
        recipe.setTransportCost(nvl(request.transportCost()));
        recipe.setActive(request.active() == null ? recipe.getActive() : request.active());

        recipePricingService.recalculate(recipe);

        return  recipeMapper.toReponse(recipeRepository.save(recipe));
    }

    @Transactional
    public void delete(UUID id, UUID userId){
        Recipe recipe = recipeRepository.findByIdAndUser_Id(id, userId)
                .orElseThrow(() -> new ResouceNotFoundExceptio("Receita não encontrada"));
        recipeRepository.delete(recipe);
    }

    @Transactional(readOnly = true)
    public RecipeResponse findById(UUID id, UUID userId) {
        return recipeRepository.findByIdAndUser_Id(id, userId)
                .map(recipeMapper::toReponse)
                .orElseThrow(() -> new ResouceNotFoundExceptio("Receita não encontrada"));
    }

    private BigDecimal nvl(BigDecimal value){
        return value == null ? BigDecimal.ZERO : value;
    }
}
