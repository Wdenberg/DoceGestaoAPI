package com.wdenberg.docegestao.auth.service;
import com.wdenberg.docegestao.auth.dto.*;
import com.wdenberg.docegestao.security.config.SecurityProperties;
import com.wdenberg.docegestao.security.jwt.JwtService;
import com.wdenberg.docegestao.user.entity.RoleName;
import com.wdenberg.docegestao.user.entity.User;
import com.wdenberg.docegestao.user.repository.RoleRepository;
import com.wdenberg.docegestao.user.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Set;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    private final RefreshTokenService refreshTokenService;
    private final SecurityProperties securityProperties;

    public AuthService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtService jwtService, RefreshTokenService refreshTokenService, SecurityProperties securityProperties) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.securityProperties = securityProperties;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email já cadastrado");
        }

        var roleUser = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new IllegalStateException("Role ROLE_USER não encontrada"));

        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.getRoles().add(roleUser);

        user = userRepository.save(user);
        var refreshToken = refreshTokenService.create(user);
        return buildAuthResponse(user, refreshToken.getToken());
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        var user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("Credenciais inválidas"));

        var refreshToken = refreshTokenService.create(user);
        return buildAuthResponse(user, refreshToken.getToken());
    }

    
    @Transactional(readOnly = true)
    public TokenResponse refresh(RefreshTokenRequest requestToken){
        var refreshToken = refreshTokenService.validate(requestToken.refreshToken());
        var user = refreshToken.getUser();

       String accessToken = jwtService.generateToken(
               user.getEmail(), 
               Map.of(
                       "userId", user.getId().toString(),
                       "name", user.getName(),
                       "roles", user.getRoles().stream().map(r ->
                           r.getName().name()
                       ).toList()
               )
       );
       return  new TokenResponse(
               accessToken,
               refreshToken.getToken(),
               "Bearer",
               securityProperties.accesTokenExpirationMinutes() * 60
       );

    }

    @Transactional
    public void logout(String refreshToken) {
        refreshTokenService.revoke(refreshToken);
    }


    private AuthResponse buildAuthResponse(User user, String refreshToken) {
        Set<String> roles = user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(java.util.stream.Collectors.toSet());

        String token = jwtService.generateToken(
                user.getEmail(),
                Map.of(
                        "userId", user.getId().toString(),
                        "roles", roles,
                        "name", user.getName()

                )
        );

        return new AuthResponse(
                token,
                refreshToken,
                "Bearer",
                securityProperties.accesTokenExpirationMinutes()  * 60,
                user.getName(),
                user.getEmail(),
                roles
        );
    }
}
