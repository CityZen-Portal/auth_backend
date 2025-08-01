package com.cityzen.auth.dto;

import lombok.Data;

@Data
public class CitizenProfileResponse {
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