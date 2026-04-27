package com.wdenberg.docegestao.auth.api;

import com.wdenberg.docegestao.auth.dto.*;
import com.wdenberg.docegestao.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth", description = "Operações de Autenticação")
public class AuthController {

    private final AuthService authService;


    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    @Operation(summary = "Registrar usuário", description = "Registra um novo usuário")
    @ApiResponse(responseCode = "201", description = "Usuário registrado com sucesso")
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @Operation(summary = "Login usuário", description = "Autentica um usuário")
    @ApiResponse(responseCode = "200", description = "Login realizado com sucesso")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @Operation(summary = "Autenticar usuário")
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@Valid @RequestBody RefreshTokenRequest requestToke){
        return ResponseEntity.ok(authService.refresh(requestToke));

    }
    @Operation(summary = "Encerrar sessão")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody RefreshTokenRequest requestToke){

        authService.logout(requestToke.refreshToken());
        return  ResponseEntity.noContent().build();
    }
}