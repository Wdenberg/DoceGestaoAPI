package com.wdenberg.docegestao.auth.service;


import com.wdenberg.docegestao.auth.RefreshToken;
import com.wdenberg.docegestao.auth.repository.RefreshTokenRepository;
import com.wdenberg.docegestao.common.exception.BusinessException;
import com.wdenberg.docegestao.security.config.SecurityProperties;
import com.wdenberg.docegestao.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final SecurityProperties securityProperties;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository,
                               SecurityProperties securityProperties) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.securityProperties = securityProperties;
    }

    @Transactional
    public RefreshToken create(User user){
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID() + "-" + UUID.randomUUID());

        refreshToken.setExpiresAt(OffsetDateTime.now().plusDays(securityProperties.accessTokenExpirationMinutes()));
        refreshToken.setRevoked(false);


       return refreshTokenRepository.save(refreshToken);
    }

    @Transactional(readOnly = true)
    public RefreshToken validate(String token){
        RefreshToken refreshToken = refreshTokenRepository.findByTokenAndRevokedFalse(token)
                .orElseThrow(() -> new BusinessException("Refresh token inválido ou expirado"));

        if(refreshToken.getExpiresAt().isBefore(OffsetDateTime.now())){
            refreshToken.setRevoked(true);
            throw new BusinessException("Refresh token inválido ou expirado");
        }

        return refreshToken;
    }

    @Transactional
    public  void revoke(String token){
        refreshTokenRepository.findByTokenAndRevokedFalse(token)
                .ifPresent(rt -> {
                    rt.setRevoked(true);
                    refreshTokenRepository.save(rt);
                });
    }
}
