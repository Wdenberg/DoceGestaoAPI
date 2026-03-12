package com.wdenberg.docegestao.recipe.api;


import com.wdenberg.docegestao.recipe.dto.RecipeItemRequest;
import com.wdenberg.docegestao.recipe.dto.RecipeRequest;
import com.wdenberg.docegestao.recipe.dto.RecipeResponse;
import com.wdenberg.docegestao.recipe.service.RecipeService;
import com.wdenberg.docegestao.security.service.AuthenticatedUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/recipes")
@Tag(name = "Recipes", description = "Endpoint de Operações de gerenciamento de receitas")
@AllArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;
    private final AuthenticatedUserService authenticatedUserService;



    @Operation(summary = "Cria Receita", description = "Cria uma nova receita")
    @PostMapping
    public ResponseEntity<RecipeResponse> create(@Valid @RequestBody RecipeRequest request){
        var currentUser = authenticatedUserService.getCurrentUser();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(recipeService.create(currentUser.id(), request));
    }

    @Operation(summary = "Adicionar item a receita")
    @PostMapping("/{id}/items")
    public ResponseEntity<RecipeResponse> addItem(
            @PathVariable UUID id, @Valid @RequestBody RecipeItemRequest request
    ){

        var currentUser = authenticatedUserService.getCurrentUser();
        return ResponseEntity.ok(recipeService.addItem(id, currentUser.id(), request));
    }

    @Operation(summary = "Lista receita com filtros")
    @GetMapping
    public ResponseEntity<Page<RecipeResponse>> findAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) String category,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)Pageable pageable
            ){
        var currentUser = authenticatedUserService.getCurrentUser();
        return ResponseEntity.ok(recipeService.findAll(currentUser.id(), name, active, category, pageable));


    }

    @Operation(summary = "Busca recita por ID")
    @GetMapping("/{id}")
    public ResponseEntity<RecipeResponse> findById(@PathVariable UUID id){
        var currentUser = authenticatedUserService.getCurrentUser();
        return ResponseEntity.ok(recipeService.findById(id, currentUser.id()));

    }

    @Operation(summary = "Atualiza Receita")
    @PutMapping("/{id}")
    public ResponseEntity<RecipeResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody RecipeRequest request
    ){
        var currentUser = authenticatedUserService.getCurrentUser();
        return  ResponseEntity.ok(recipeService.update(id, currentUser.id(), request));
    }

    @Operation(summary = "Remover Receita")
    @DeleteMapping("/{id}")
    public ResponseEntity<RecipeResponse> delete(
            @PathVariable UUID id,
            @Valid @RequestBody RecipeRequest request
    ){
        var currentUser = authenticatedUserService.getCurrentUser();
        recipeService.delete(id, currentUser.id());
        return ResponseEntity.noContent().build();
    }
}
