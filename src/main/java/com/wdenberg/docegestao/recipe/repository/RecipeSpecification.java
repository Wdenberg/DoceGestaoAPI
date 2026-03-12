package com.wdenberg.docegestao.recipe.repository;

import com.wdenberg.docegestao.recipe.entity.Recipe;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class RecipeSpecification {

    public static Specification<Recipe> byFilters(UUID userId, String name, String category, Boolean active){


        return (root, query, cb) -> {

            var predicate = cb.equal(root.get("user").get("id"), userId);
            if(name != null && !name.isBlank()){
                predicate = cb.and(predicate, cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));

            }
            if(active != null){
                predicate = cb.and(predicate, cb.equal(root.get("active"), active));

            }
            if(category != null && !name.isBlank()){
                predicate = cb.and(predicate, cb.equal(cb.lower(root.get("category")), category.toLowerCase()));
            }

            return predicate;
        };
    }
}
