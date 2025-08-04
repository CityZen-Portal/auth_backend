package com.cityzen.auth.service;

import com.cityzen.auth.payload.ApiResponse;
import com.cityzen.auth.dto.*;

import org.springframework.security.core.Authentication;

public interface AuthService {
    boolean verifyAadhaar(String aadhaar);
    JwtResponse login(SignInRequest request);
    JwtResponse generateJwtResponse(Authentication authentication);
    void forgotPassword(ForgotPasswordRequest request);
    void resetPassword(ResetPasswordRequest request);
    void changePassword(ChangePasswordRequest request);
    ApiResponse register(SignUpRequest request);
    int getCitizenCount();
    JwtResponse refreshToken(RefreshTokenRequest request);
    int getgenderCount(String gender );
}
