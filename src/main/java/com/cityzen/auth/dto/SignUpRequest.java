package com.cityzen.auth.dto;
import com.cityzen.auth.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class
SignUpRequest {
    @NotBlank
    private String userName;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String aadhaar;
    private Role role;;
    private String gender;

    public boolean isValidPassword() {
        return password != null;
    }
}