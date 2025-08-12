package com.cityzen.auth.service;

import com.cityzen.auth.entity.User;
import com.cityzen.auth.payload.ApiResponse;
import com.cityzen.auth.dto.*;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

import java.util.Optional;

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
    Long doesUserExist(String email);
    User getUserById(Long userId);
    ApiResponse staffPasswordUpdate(String email, String password);
    boolean deleteStaff(String email);
}
