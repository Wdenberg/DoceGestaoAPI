package com.wdenberg.docegestao.ingredient.service;


import com.wdenberg.docegestao.ingredient.dto.IngredientRequest;
import com.wdenberg.docegestao.ingredient.dto.IngredientResponse;
import com.wdenberg.docegestao.ingredient.entity.Ingredient;
import com.wdenberg.docegestao.ingredient.mapper.IngredientMapper;
import com.wdenberg.docegestao.ingredient.repository.IngredientRepository;
import com.wdenberg.docegestao.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

@Service
public class IngredientService {

    private final IngredientRepository ingredientRepository;
    private final UserRepository userRepository;
    private final IngredientMapper ingredientMapper;

    public IngredientService(IngredientRepository ingredientRepository,
                             UserRepository userRepository,
                             IngredientMapper ingredientMapper) {
        this.ingredientRepository = ingredientRepository;
        this.userRepository = userRepository;
        this.ingredientMapper = ingredientMapper;
    }

    @Transactional
    public IngredientResponse create(UUID userId, IngredientRequest request) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        Ingredient ingredient = new Ingredient();
        ingredient.setUser(user);
        ingredient.setName(request.name());
        ingredient.setQuantity(request.quantity());
        ingredient.setUnitMeasure(request.unitMeasure());
        ingredient.setPurchasePrice(request.purchasePrice());
        ingredient.setSupplier(request.supplier());
        ingredient.setActive(request.active() == null ? Boolean.TRUE : request.active());
        ingredient.setCostPerUnit(calculateCostPerUnit(request.purchasePrice(), request.quantity()));

        return ingredientMapper.toResponse(ingredientRepository.save(ingredient));
    }

    @Transactional(readOnly = true)
    public List<IngredientResponse> findAll(UUID userId) {
        return ingredientRepository.findAllByUserId(userId)
                .stream()
                .map(ingredientMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public IngredientResponse findById(UUID id, UUID userId) {
        return ingredientRepository.findByIdAndUserId(id, userId)
                .map(ingredientMapper::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("Ingrediente não encontrado"));
    }

    @Transactional
    public IngredientResponse update(UUID id, UUID userId, IngredientRequest request) {
        var ingredient = ingredientRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new IllegalArgumentException("Ingrediente não encontrado"));

        ingredient.setName(request.name());
        ingredient.setQuantity(request.quantity());
        ingredient.setUnitMeasure(request.unitMeasure());
        ingredient.setPurchasePrice(request.purchasePrice());
        ingredient.setSupplier(request.supplier());
        ingredient.setActive(request.active() == null ? ingredient.getActive() : request.active());
        ingredient.setCostPerUnit(calculateCostPerUnit(request.purchasePrice(), request.quantity()));

        return ingredientMapper.toResponse(ingredientRepository.save(ingredient));
    }

    @Transactional
    public void delete(UUID id, UUID userId) {
        var ingredient = ingredientRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new IllegalArgumentException("Ingrediente não encontrado"));
        ingredientRepository.delete(ingredient);
    }

    private BigDecimal calculateCostPerUnit(BigDecimal purchasePrice, BigDecimal quantity) {
        return purchasePrice.divide(quantity, 6, RoundingMode.HALF_UP);
    }
}