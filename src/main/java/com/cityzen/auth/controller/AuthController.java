package com.cityzen.auth.controller;

import com.cityzen.auth.entity.User;
import com.cityzen.auth.payload.ApiResponse;
import com.cityzen.auth.dto.*;
import com.cityzen.auth.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private OtpService otpService;
    @Autowired
    private AadhaarRegistryService service;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserValidationService userValidationService;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @PostMapping("/validate-user-and-aadhaar")
    public ResponseEntity<ApiResponse<?>> validateUserAndAadhaar(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody FileUploadDto fileUploadDto,
            HttpServletRequest request) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(401, "Missing or invalid Authorization header", null, request.getRequestURI()));
        }

        String token = authHeader.substring(7);
        TokenResponseDto tokenResponseDto = userValidationService.validateUser(token);
        if (!tokenResponseDto.isValid()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(401, "UNAUTHORIZED USER", null, request.getRequestURI()));
        }

        boolean aadhaarExists = authService.verifyAadhaar(fileUploadDto.getAadharNumber());
        if (!aadhaarExists) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(404, "AADHAAR NOT FOUND", null, request.getRequestURI()));
        }

        return ResponseEntity.ok(new ApiResponse<>(200, "SUCCESS", null, request.getRequestURI()));
    }

    @PostMapping("/verify-aadhaar")
    public ResponseEntity<ApiResponse<Boolean>> verifyAadhaar(@RequestBody AadhaarVerifyRequest request, HttpServletRequest httpRequest) {
        boolean isVerified = authService.verifyAadhaar(request.getAadhaar());
        if (isVerified) {
            return ResponseEntity.ok(new ApiResponse<>(200, "Aadhaar number is verified.", true, httpRequest.getRequestURI()));
        } else {
            return ResponseEntity.status(404).body(new ApiResponse<>(404, "Aadhaar number not found.", false, httpRequest.getRequestURI()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register(@RequestBody SignUpRequest request, HttpServletRequest httpRequest) {
        try {
            String aadhaar = request.getAadhaar();
            if (aadhaar == null || !aadhaar.matches("\\d{12}")) {
                return ResponseEntity.status(400)
                        .body(new ApiResponse<>(400, "Aadhaar number must be exactly 12 digits", null,httpRequest.getRequestURI()));
            }
            if (!authService.verifyAadhaar(aadhaar)) {
                return ResponseEntity.status(400)
                        .body(new ApiResponse<>(400, "Aadhaar number not verified or not found", null, httpRequest.getRequestURI()));
            }
            ApiResponse<?> response = authService.register(request);
            response.setApi(httpRequest.getRequestURI());
            return ResponseEntity.status(response.getStatus()).body(response);
        } catch (Exception e) {
            logger.error("Signup failed: {}", e.getMessage());
            System.out.println(e.getMessage());
            return ResponseEntity.status(500)
                    .body(new ApiResponse<>(500, "Registration failed: " + e.getMessage(), null, httpRequest.getRequestURI()));
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin-only")
    public ResponseEntity<ApiResponse<String>> adminEndpoint(@RequestParam String data, HttpServletRequest httpRequest) {
        ApiResponse<String> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Admin access granted",
                data,
                httpRequest.getRequestURI()
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponse>> login(@RequestBody SignInRequest request, HttpServletRequest httpRequest) {
        try {
            JwtResponse jwtResponse = authService.login(request);
            ApiResponse<JwtResponse> response = new ApiResponse<>(
                    HttpStatus.OK.value(),
                    "Login successful",
                    jwtResponse,
                    httpRequest.getRequestURI()
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Signin failed: {}", e.getMessage());
            return ResponseEntity.status(401).body(new ApiResponse<>(401, "Login failed: " + e.getMessage(), null, httpRequest.getRequestURI()));
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<String>> forgotPassword(@RequestBody ForgotPasswordRequest request, HttpServletRequest httpRequest) {
        try {
            authService.forgotPassword(request);
            return ResponseEntity.ok(new ApiResponse<>(200, "Password reset link sent", null, httpRequest.getRequestURI()));
        } catch (Exception e) {
            logger.error("Forgot password failed: {}", e.getMessage());
            return ResponseEntity.status(500).body(new ApiResponse<>(500, "Failed to send password reset email: " + e.getMessage(), null, httpRequest.getRequestURI()));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<String>> resetPassword(@RequestBody ResetPasswordRequest request, HttpServletRequest httpRequest) {
        try {
            authService.resetPassword(request);
            return ResponseEntity.ok(new ApiResponse<>(200, "Password reset successfully", null, httpRequest.getRequestURI()));
        } catch (Exception e) {
            logger.error("Reset password failed: {}", e.getMessage());
            return ResponseEntity.status(400).body(new ApiResponse<>(400, "Password reset failed: " + e.getMessage(), null, httpRequest.getRequestURI()));
        }
    }

    @PutMapping("/change-password")
    public ResponseEntity<ApiResponse<String>> changePassword(@RequestBody ChangePasswordRequest request, HttpServletRequest httpRequest) {
        try {
            authService.changePassword(request);
            return ResponseEntity.ok(new ApiResponse<>(200, "Password changed successfully", null, httpRequest.getRequestURI()));
        } catch (Exception e) {
            logger.error("Change password failed: {}", e.getMessage());
            return ResponseEntity.status(400).body(new ApiResponse<>(400, "Password change failed: " + e.getMessage(), null, httpRequest.getRequestURI()));
        }
    }

    @PostMapping("/generate-otp")
    public ResponseEntity<ApiResponse<String>> generateOtp(@RequestBody EmailRequest request, HttpServletRequest httpRequest) {
        try {
            String otp = otpService.generateOtp(request.getEmail());
            emailService.sendOtp(request.getEmail(), otp);
            return ResponseEntity.ok(new ApiResponse<>(200, "OTP sent to " + request.getEmail(), null, httpRequest.getRequestURI()));
        } catch (Exception e) {
            logger.error("Generate OTP failed: {}", e.getMessage());
            return ResponseEntity.status(500).body(new ApiResponse<>(500, "Failed to generate OTP: " + e.getMessage(), null, httpRequest.getRequestURI()));
        }
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<ApiResponse<String>> resendOtp(@RequestBody EmailRequest request, HttpServletRequest httpRequest) {
        try {
            String otp = otpService.resendOtp(request.getEmail());
            emailService.sendOtp(request.getEmail(), otp);
            return ResponseEntity.ok(new ApiResponse<>(200, "OTP resent to " + request.getEmail(), null, httpRequest.getRequestURI()));
        } catch (Exception e) {
            logger.error("Resend OTP failed: {}", e.getMessage());
            return ResponseEntity.status(500).body(new ApiResponse<>(500, "Failed to resend OTP: " + e.getMessage(), null, httpRequest.getRequestURI()));
        }
    }

    @PostMapping("/validate-otp")
    public ResponseEntity<ApiResponse<Boolean>> validateOtp(@RequestBody ValidateOtpRequest request, HttpServletRequest httpRequest) {
        boolean isValid = otpService.validateOtp(request.getEmail(), request.getOtp());
        if (isValid) {
            return ResponseEntity.ok(new ApiResponse<>(200, "OTP is valid", true, httpRequest.getRequestURI()));
        } else {
            return ResponseEntity.status(400).body(new ApiResponse<>(400, "Invalid OTP", false, httpRequest.getRequestURI()));
        }
    }

    @PostMapping("/add-aadhaar")
    public ResponseEntity<ApiResponse<String>> addAadhaar(@RequestBody AadhaarRequest request, HttpServletRequest httpRequest) {
        String aadhaar = request.getAadhaar();
        if (aadhaar == null || !aadhaar.matches("\\d{12}")) {
            return ResponseEntity.status(400)
                    .body(new ApiResponse<>(400, "Aadhaar number must be exactly 12 digits", null, httpRequest.getRequestURI()));
        }
        String response = service.saveAadhaar(aadhaar);
        return ResponseEntity.ok(new ApiResponse<>(200, response, null, httpRequest.getRequestURI()));
    }

    @GetMapping("/get-count/citizen")
    public ResponseEntity<ApiResponse<Integer>> getCitizenCount(HttpServletRequest httpRequest) {
        int count = authService.getCitizenCount();
        ApiResponse<Integer> response = new ApiResponse<>(
                200,
                "OK",
                count,
                httpRequest.getRequestURI()
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<JwtResponse>> refreshToken(@RequestBody RefreshTokenRequest request, HttpServletRequest httpRequest) {
        try {
            JwtResponse responseData = authService.refreshToken(request);
            ApiResponse<JwtResponse> response = new ApiResponse<>(200, "Token refreshed", responseData, httpRequest.getRequestURI());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Refresh token failed: {}", e.getMessage());
            return ResponseEntity.status(401).body(new ApiResponse<>(401, "Refresh token failed: " + e.getMessage(), null, httpRequest.getRequestURI()));
        }
    }

    @GetMapping("/get-count/gender")
    public ResponseEntity<ApiResponse<?>> getGenderCount(HttpServletRequest httpRequest) {
        try {
            int maleCount = authService.getgenderCount("male");
            int femaleCount = authService.getgenderCount("female");
            int otherCount = authService.getgenderCount("other");
            GenderCountDto count = new GenderCountDto();
            count.setMaleCount(maleCount);
            count.setFemaleCount(femaleCount);
            count.setOtherCount(otherCount);
            ApiResponse<GenderCountDto> response = new ApiResponse<>(200, "OK", count, httpRequest.getRequestURI());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(new ApiResponse<>(500, "Internal Server Error", e.getMessage(), httpRequest.getRequestURI()));
        }
    }

    @GetMapping("/getUser/{email}")
    public ResponseEntity<ApiResponse<?>> getUser(@PathVariable String email, HttpServletRequest httpRequest) {
        try {
            if(Objects.equals(email, "") || email == null){
                ResponseEntity<ApiResponse<?>> invalidEmail = ResponseEntity.status(400).body(new ApiResponse<>(400, "Invalid email", null, httpRequest.getRequestURI()));
                return invalidEmail;
            }
            User user = customUserDetailsService.getUserByEmail(email);
            return ResponseEntity.ok(new ApiResponse<>(200, "OK", user, httpRequest.getRequestURI()));
        } catch (Exception e) {
            ResponseEntity<ApiResponse<?>> internalServerError = ResponseEntity.status(500).body(new ApiResponse<>(500, "Internal Server Error", e.getMessage(), httpRequest.getRequestURI()));
            return internalServerError;
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<TokenResponseDto> validateUser(@RequestHeader("token") String token) {
        try {
            TokenResponseDto response = userValidationService.validateUser(token);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(new TokenResponseDto(false, null, "Invalid or expired token"));
        }
    }

    @PutMapping("/staff/reset")
    public ResponseEntity<ApiResponse<?>> resetPassword(@RequestBody ResetStaffPasswordRequest request) {
        ApiResponse<?> response = authService.staffPasswordUpdate(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{email}")
    public boolean deleteStaff(@PathVariable String email)
    {
        return authService.deleteStaff(email);
    }

}