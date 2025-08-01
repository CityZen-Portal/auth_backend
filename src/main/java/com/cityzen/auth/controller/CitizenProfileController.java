package com.cityzen.auth.controller;

import com.cityzen.auth.dto.AdminProfileResponse;
import com.cityzen.auth.dto.CitizenProfileResponse;
import com.cityzen.auth.entity.AdminProfile;
import com.cityzen.auth.entity.CitizenProfile;
import com.cityzen.auth.service.CitizenProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/citizen-profiles")
public class CitizenProfileController {

    @Autowired
    private CitizenProfileService citizenProfileService;

    @PostMapping
    public CitizenProfileResponse create(@RequestBody CitizenProfile profile) {
        CitizenProfile created = citizenProfileService.createProfile(profile);
        return toResponse(created);
    }

    @GetMapping("/{citizenId}")
    public CitizenProfileResponse get(@PathVariable String citizenId) {
        CitizenProfile profile = citizenProfileService.getProfileByCitizenId(citizenId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
        return toResponse(profile);
    }

    @PutMapping("/{citizenId}")
    public CitizenProfileResponse update(@PathVariable String citizenId, @RequestBody CitizenProfile updated) {
        CitizenProfile profile = citizenProfileService.updateProfile(citizenId, updated);
        return toResponse(profile);
    }

    private CitizenProfileResponse toResponse(CitizenProfile profile) {
        CitizenProfileResponse response = new CitizenProfileResponse();
        response.setCitizenId(profile.getCitizenId());
        response.setUserName(profile.getUserName());
        response.setEmail(profile.getEmail());
        response.setGender(profile.getGender());
        response.setDob(profile.getDob());
        response.setAddress(profile.getAddress());
        response.setCity(profile.getCity());
        response.setState(profile.getState());
        response.setPincode(profile.getPincode());
        response.setAadhaar(profile.getAadhaar());
        return response;
    }
}