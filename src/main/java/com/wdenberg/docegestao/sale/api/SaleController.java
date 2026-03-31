package com.wdenberg.docegestao.sale.api;

import com.wdenberg.docegestao.sale.dto.SaleItemRequest;
import com.wdenberg.docegestao.sale.dto.SaleRequest;
import com.wdenberg.docegestao.sale.dto.SaleResponse;
import com.wdenberg.docegestao.sale.entity.SaleStatus;
import com.wdenberg.docegestao.sale.service.SaleService;
import com.wdenberg.docegestao.security.service.AuthenticatedUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/sales")
@Tag(name = "Sale", description = "Operações de Gerenciamento de Vendas")
public class SaleController {


    private final SaleService saleService;
    private final AuthenticatedUserService authenticatedUserService;

    public SaleController(SaleService saleService,
                          AuthenticatedUserService authenticatedUserService) {
        this.saleService = saleService;
        this.authenticatedUserService = authenticatedUserService;
    }

    @Operation(summary = "Criar venda")
    @PostMapping
    public ResponseEntity<SaleResponse> create(@Valid @RequestBody SaleRequest request) {
        var currentUser = authenticatedUserService.getCurrentUser();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(saleService.create(currentUser.id(), request));
    }

    @Operation(summary = "Adicionar item à venda")
    @PostMapping("/{id}/items")
    public ResponseEntity<SaleResponse> addItem(@PathVariable UUID id,
                                                @Valid @RequestBody SaleItemRequest request) {
        var currentUser = authenticatedUserService.getCurrentUser();
        return ResponseEntity.ok(saleService.addItem(id, currentUser.id(), request));
    }

    @Operation(summary = "Atualizar status da venda")
    @PatchMapping("/{id}/status")
    public ResponseEntity<SaleResponse> updateStatus(@PathVariable UUID id,
                                                     @RequestParam SaleStatus status) {
        var currentUser = authenticatedUserService.getCurrentUser();
        return ResponseEntity.ok(saleService.updateStatus(id, currentUser.id(), status));
    }

    @Operation(summary = "Listar vendas com filtros")
    @GetMapping
    public ResponseEntity<Page<SaleResponse>> findAll(
            @RequestParam(required = false) SaleStatus status,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        var currentUser = authenticatedUserService.getCurrentUser();
        return ResponseEntity.ok(saleService.findAll(currentUser.id(), status, startDate, endDate, pageable));
    }

    @Operation(summary = "Buscar venda por ID")
    @GetMapping("/{id}")
    public ResponseEntity<SaleResponse> findById(@PathVariable UUID id) {
        var currentUser = authenticatedUserService.getCurrentUser();
        return ResponseEntity.ok(saleService.findById(id, currentUser.id()));
    }
}
