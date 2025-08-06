package com.cityzen.auth.service;

import com.cityzen.auth.dto.TokenResponseDto;
import com.cityzen.auth.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserValidationService {

    @Autowired
    private JwtUtil jwtUtil;

    public TokenResponseDto validateUser(String token) {
        TokenResponseDto response = new TokenResponseDto();
        try {
            String email = jwtUtil.extractUsername(token);
            boolean valid = jwtUtil.validateToken(token, email);
            response.setValid(valid);
            response.setEmail(valid ? email : null);
        } catch (Exception e) {
            response.setValid(false);
            response.setEmail(null);
        }
        return response;
    }
}