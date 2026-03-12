package com.wdenberg.docegestao.recipe.repository;

import com.wdenberg.docegestao.recipe.entity.Recipe;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface RecipeRepository extends JpaRepository<Recipe, UUID>, JpaSpecificationExecutor<Recipe> {

    @EntityGraph(attributePaths = {"items", "items.ingredient"})
    Optional<Recipe> findByIdAndUser_Id(UUID id, UUID userId);
}
