package com.cityzen.auth.dto;

import com.cityzen.auth.enums.Gender;
import lombok.Data;
import java.time.LocalDate;

@Data
public class AdminProfileResponse {
    private String adminId;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate dob;
    private Gender gender;
    private String designation;
    private LocalDate joinedAt;
    private String address;
    private String city;
    private String state;
    private String pincode;
    private String phoneNo;
}