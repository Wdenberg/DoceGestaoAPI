package com.wdenberg.docegestao.client.api;

import com.wdenberg.docegestao.client.dto.ClientRequest;
import com.wdenberg.docegestao.client.dto.ClientResponse;
import com.wdenberg.docegestao.client.service.ClientService;
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
@RequestMapping("/api/v1/clients")
@Tag(name = "Clients", description = "Endpoint de Operações de gerenciamento de clientes")
@AllArgsConstructor
public class ClientController {

    private final ClientService clientService;
    private final AuthenticatedUserService authenticatedUserService;

    @Operation(summary = "Cria Cliente", description = "Cria um novo cliente")
    @PostMapping
    public ResponseEntity<ClientResponse> crate(@Valid @RequestBody ClientRequest request){
        var currentUser = authenticatedUserService.getCurrentUser();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(clientService.crate(currentUser.id(), request));
    }

    @Operation(summary = "Lista clientes com filtros")
    @GetMapping
    public ResponseEntity<Page<ClientResponse>> findAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Boolean active,
            @PageableDefault(size = 10, sort = "cratedAt", direction = Sort.Direction.DESC)Pageable pageable
            ){
        var currentUser = authenticatedUserService.getCurrentUser();
        return ResponseEntity.ok(clientService.findAll(currentUser.id(), name, active, pageable));
    }

    @Operation(summary = "Buscar cliente por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> findById(@PathVariable UUID id) {
        var currentUser = authenticatedUserService.getCurrentUser();
        return ResponseEntity.ok(clientService.findById(id, currentUser.id()));
    }

    @Operation(summary = "Atualizar cliente")
    @PutMapping("/{id}")
    public ResponseEntity<ClientResponse> update(@PathVariable UUID id,
                                                 @Valid @RequestBody ClientRequest request) {
        var currentUser = authenticatedUserService.getCurrentUser();
        return ResponseEntity.ok(clientService.update(id, currentUser.id(), request));
    }

    @Operation(summary = "Remover cliente")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        var currentUser = authenticatedUserService.getCurrentUser();
        clientService.delete(id, currentUser.id());
        return ResponseEntity.noContent().build();
    }
}
