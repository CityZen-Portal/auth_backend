package com.cityzen.auth.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class CitizenProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String citizenId;
    private String userName;
    private String email;
    private String aadhaar;
    private String gender;
    private String address;
    private String city;
    private String state;
    private String pincode;
    private String dob;
}