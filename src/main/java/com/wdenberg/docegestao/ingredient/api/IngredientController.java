package com.wdenberg.docegestao.ingredient.api;

import com.wdenberg.docegestao.ingredient.dto.IngredientRequest;
import com.wdenberg.docegestao.ingredient.dto.IngredientResponse;
import com.wdenberg.docegestao.ingredient.service.IngredientService;
import com.wdenberg.docegestao.security.service.AuthenticatedUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/ingredients")
@Tag(name = "Ingredient", description = "Operações de Gerenciamento de Ingredientes")
public class IngredientController {

    private final IngredientService ingredientService;
    private final AuthenticatedUserService authenticatedUserService;

    public IngredientController(IngredientService ingredientService,
                                AuthenticatedUserService authenticatedUserService) {
        this.ingredientService = ingredientService;
        this.authenticatedUserService = authenticatedUserService;
    }

    @Operation(summary = "Criar ingrediente")
    @PostMapping
    public ResponseEntity<IngredientResponse> create(@Valid @RequestBody IngredientRequest request) {
        var currentUser = authenticatedUserService.getCurrentUser();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ingredientService.create(currentUser.id(), request));
    }

    @Operation(summary = "Listar ingredientes")
    @GetMapping
    public ResponseEntity<List<IngredientResponse>> findAll() {
        var currentUser = authenticatedUserService.getCurrentUser();
        return ResponseEntity.ok(ingredientService.findAll(currentUser.id()));
    }

    @Operation(summary = "Buscar ingrediente por ID")
    @GetMapping("/{id}")
    public ResponseEntity<IngredientResponse> findById(@PathVariable UUID id) {
        var currentUser = authenticatedUserService.getCurrentUser();
        return ResponseEntity.ok(ingredientService.findById(id, currentUser.id()));
    }

    @Operation(summary = "Atualizar ingrediente")
    @PutMapping("/{id}")
    public ResponseEntity<IngredientResponse> update(@PathVariable UUID id,
                                                     @Valid @RequestBody IngredientRequest request) {
        var currentUser = authenticatedUserService.getCurrentUser();
        return ResponseEntity.ok(ingredientService.update(id, currentUser.id(), request));
    }

    @Operation(summary = "Remover ingrediente")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        var currentUser = authenticatedUserService.getCurrentUser();
        ingredientService.delete(id, currentUser.id());
        return ResponseEntity.noContent().build();
    }


}