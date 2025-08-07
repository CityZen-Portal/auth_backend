package com.cityzen.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
    private Long id;
    private String accessToken;
    private String refreshToken;
    private List<String> roles;
    private String email;
    private long expiry;
}