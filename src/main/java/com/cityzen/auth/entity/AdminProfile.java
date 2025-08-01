package com.cityzen.auth.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
public class AdminProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String adminId;

    private String firstName;
    private String lastName;
    private String email;
    private String gender;
    private LocalDate dob;
    private String address;
    private String city;
    private String state;
    private String pincode;
    private String phoneNo;

    @Enumerated(EnumType.STRING)
    private AccessLevel accessLevel;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime lastLogin;

    public enum AccessLevel {
        ADMIN,
        SUPER_ADMIN,
        SERVICE_MANAGER,
        COMPLAINT_MANAGER,
        READ_ONLY_ADMIN
    }

    public enum Status {
        ACTIVE,
        INACTIVE,
        SUSPENDED
    }
}