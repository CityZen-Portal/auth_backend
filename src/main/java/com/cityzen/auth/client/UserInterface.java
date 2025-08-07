package com.cityzen.auth.client;

import com.cityzen.auth.payload.ApiResponse;
import com.cityzen.auth.dto.TokenResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import com.cityzen.auth.dto.EmailResponseDto;

@FeignClient(name = "UserManagementService", url = "https://auth-backend-2-k3ph.onrender.com")
public interface UserInterface {
    @GetMapping("/api/auth/validate")
    ResponseEntity<TokenResponseDto> validateUser(@RequestHeader("token") String token);

    @GetMapping("/api/auth/getUser/{email}")
    ResponseEntity<ApiResponse<EmailResponseDto>> getProfileByEmail(@PathVariable("email") String emailId);
}