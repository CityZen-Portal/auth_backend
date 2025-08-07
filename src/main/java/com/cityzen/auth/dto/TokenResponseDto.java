package com.cityzen.auth.dto;

import lombok.Data;

@Data
public class TokenResponseDto {
    private boolean valid;
    private String email;
}