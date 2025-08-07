package com.cityzen.auth.service;

import com.cityzen.auth.client.UserInterface;
import com.cityzen.auth.dto.TokenResponseDto;
import com.cityzen.auth.payload.ApiResponse;
import com.cityzen.auth.dto.EmailResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ExternalUserService {

    @Autowired
    private UserInterface userInterface;

    public TokenResponseDto validateUser(String token) {
        ResponseEntity<TokenResponseDto> response = userInterface.validateUser(token);
        return response.getBody();
    }

    public EmailResponseDto getProfileByEmail(String email) {
        ResponseEntity<ApiResponse<EmailResponseDto>> response = userInterface.getProfileByEmail(email);
        return response.getBody().getData();
    }
}