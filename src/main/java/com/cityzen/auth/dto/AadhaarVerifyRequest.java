package com.cityzen.auth.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AadhaarVerifyRequest {

    @NotBlank
    private String aadhaar;
    @Email
    @NotBlank
    private String email;
}
