package com.cityzen.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ValidateOtpRequest {

    @NotBlank
    private String email;

    @NotBlank
    private String otp;
}
