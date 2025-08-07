package com.cityzen.auth.dto;

import com.cityzen.auth.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailResponseDto {
    private Long id;
    private String email;
    private String userName;
    private Set<Role> role;
    private String aadhaarNumber;
}