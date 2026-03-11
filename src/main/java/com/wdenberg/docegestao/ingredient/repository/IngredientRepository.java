package com.wdenberg.docegestao.ingredient.repository;

import com.wdenberg.docegestao.ingredient.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IngredientRepository extends JpaRepository<Ingredient, UUID> {

    List<Ingredient> findAllByUserId(UUID userId);
    Optional<Ingredient> findByIdAndUserId(UUID id, UUID userId);
}
