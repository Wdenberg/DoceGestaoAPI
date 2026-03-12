package com.wdenberg.docegestao.recipe.repository;

import com.wdenberg.docegestao.recipe.entity.RecipeItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RecipeItemRepository extends JpaRepository<RecipeItem, UUID>  {
}
