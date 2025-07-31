package com.cityzen.auth.entity;

import com.cityzen.auth.enums.Role;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;
    private String email;
    private String password;
    private String aadhaar;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)


    private Role role;
    private String aadhaar;  
}