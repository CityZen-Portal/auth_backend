package com.cityzen.auth.service;

import com.cityzen.auth.entity.RefreshToken;
import com.cityzen.auth.entity.User;
import com.cityzen.auth.repository.RefreshTokenRepository;
import com.cityzen.auth.repository.UserRepository;
import com.cityzen.auth.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private static final long REFRESH_TOKEN_VALIDITY_SECONDS = 7 * 24 * 60 * 60; // 7 days

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private String generateTokenValue() {
        return UUID.randomUUID().toString();
    }

    public RefreshToken createRefreshToken(User user) {
        RefreshToken token = new RefreshToken();
        token.setUser(user);
        token.setToken(generateTokenValue());
        token.setExpiryDate(Instant.now().plusMillis(jwtUtil.getRefreshTokenExpirationMillis()));
        refreshTokenRepository.save(token);

        user.setRefreshToken(token.getToken());
        userRepository.save(user);

        return token;
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public boolean isExpired(RefreshToken token) {
        return token.getExpiryDate().isBefore(Instant.now());
    }

    public void deleteByUser(User user) {
        refreshTokenRepository.deleteByUser(user);
    }

    public void revokeToken(String tokenValue) {
        Optional<RefreshToken> tokenOpt = refreshTokenRepository.findByToken(tokenValue);
        if (tokenOpt.isPresent()) {
            RefreshToken token = tokenOpt.get();
            User user = token.getUser();
            refreshTokenRepository.delete(token);

            user.setRefreshToken(null);
            userRepository.save(user);
        }
    }
}