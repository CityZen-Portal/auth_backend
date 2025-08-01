package com.cityzen.auth.service;

import com.cityzen.auth.entity.AdminProfile;
import com.cityzen.auth.repository.AdminProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminProfileService {

    @Autowired
    private AdminProfileRepository repository;

    public Optional<AdminProfile> getProfileByAdminId(String adminId) {
        return repository.findByAdminId(adminId);
    }

    public AdminProfile createProfile(AdminProfile profile) {
        return repository.save(profile);
    }

    public AdminProfile updateProfile(String adminId, AdminProfile updated) {
        AdminProfile profile = repository.findByAdminId(adminId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        profile.setFirstName(updated.getFirstName());
        profile.setLastName(updated.getLastName());
        profile.setEmail(updated.getEmail());
        profile.setGender(updated.getGender());
        profile.setDob(updated.getDob());
        profile.setAddress(updated.getAddress());
        profile.setCity(updated.getCity());
        profile.setState(updated.getState());
        profile.setPincode(updated.getPincode());
        profile.setPhoneNo(updated.getPhoneNo());
        profile.setAccessLevel(updated.getAccessLevel());
        profile.setStatus(updated.getStatus());
        profile.setLastLogin(updated.getLastLogin());

        return repository.save(profile);
    }
}