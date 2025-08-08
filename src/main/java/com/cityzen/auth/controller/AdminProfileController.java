package com.cityzen.auth.controller;

import com.cityzen.auth.dto.AdminProfileResponse;
import com.cityzen.auth.entity.AdminProfile;
import com.cityzen.auth.service.AdminProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin-profiles")
public class AdminProfileController {

    @Autowired
    private AdminProfileService adminProfileService;

    @PostMapping
    public AdminProfileResponse create(@RequestBody AdminProfile profile) {
        AdminProfile created = adminProfileService.createProfile(profile);
        return toResponse(created);
    }

    @GetMapping("/{adminId}")
    public AdminProfileResponse get(@PathVariable String adminId) {
        AdminProfile profile = adminProfileService.getProfileByAdminId(adminId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
        return toResponse(profile);
    }

    @PutMapping("/{adminId}")
    public AdminProfileResponse update(@PathVariable String adminId, @RequestBody AdminProfile updated) {
        AdminProfile profile = adminProfileService.updateProfile(adminId, updated);
        return toResponse(profile);
    }

    private AdminProfileResponse toResponse(AdminProfile profile) {
        AdminProfileResponse response = new AdminProfileResponse();
        response.setAdminId(profile.getAdminId());
        response.setFirstName(profile.getFirstName());
        response.setLastName(profile.getLastName());
        response.setEmail(profile.getEmail());
        response.setDob(profile.getDob());
        response.setGender(profile.getGender());
        response.setDesignation(profile.getDesignation());
        response.setJoinedAt(profile.getJoinedAt());
        response.setAddress(profile.getAddress());
        response.setCity(profile.getCity());
        response.setState(profile.getState());
        response.setPincode(profile.getPincode());
        response.setPhoneNo(profile.getPhoneNo());
        return response;
    }
}