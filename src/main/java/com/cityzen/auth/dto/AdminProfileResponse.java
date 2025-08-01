package com.cityzen.auth.dto;

import com.cityzen.auth.entity.AdminProfile;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AdminProfileResponse {
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
    private AdminProfile.AccessLevel accessLevel;
    private AdminProfile.Status status;
    private LocalDateTime lastLogin;
}