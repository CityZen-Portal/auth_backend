package com.cityzen.auth.entity;

import com.cityzen.auth.enums.Gender;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

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

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate dob;
    private String designation;
    private LocalDate joinedAt;
    private String address;
    private String city;
    private String state;
    private String pincode;
    private String phoneNo;
}