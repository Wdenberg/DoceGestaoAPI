package com.wdenberg.docegestao.ingredient.api;

import com.wdenberg.docegestao.ingredient.dto.IngredientRequest;
import com.wdenberg.docegestao.ingredient.dto.IngredientResponse;
import com.wdenberg.docegestao.ingredient.service.IngredientService;
import com.wdenberg.docegestao.security.service.AuthenticatedUserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/ingredients")
public class IngredientController {

    private final IngredientService ingredientService;
    private final AuthenticatedUserService authenticatedUserService;

    public IngredientController(IngredientService ingredientService,
                                AuthenticatedUserService authenticatedUserService) {
        this.ingredientService = ingredientService;
        this.authenticatedUserService = authenticatedUserService;
    }

    @PostMapping
    public ResponseEntity<IngredientResponse> create(@Valid @RequestBody IngredientRequest request) {
        var currentUser = authenticatedUserService.getCurrentUser();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ingredientService.create(currentUser.id(), request));
    }

    @GetMapping
    public ResponseEntity<List<IngredientResponse>> findAll() {
        var currentUser = authenticatedUserService.getCurrentUser();
        return ResponseEntity.ok(ingredientService.findAll(currentUser.id()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<IngredientResponse> findById(@PathVariable UUID id) {
        var currentUser = authenticatedUserService.getCurrentUser();
        return ResponseEntity.ok(ingredientService.findById(id, currentUser.id()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<IngredientResponse> update(@PathVariable UUID id,
                                                     @Valid @RequestBody IngredientRequest request) {
        var currentUser = authenticatedUserService.getCurrentUser();
        return ResponseEntity.ok(ingredientService.update(id, currentUser.id(), request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        var currentUser = authenticatedUserService.getCurrentUser();
        ingredientService.delete(id, currentUser.id());
        return ResponseEntity.noContent().build();
    }
}