package com.cityzen.auth.service;

import com.cityzen.auth.entity.CitizenProfile;
import com.cityzen.auth.repository.CitizenProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CitizenProfileService {

    @Autowired
    private CitizenProfileRepository repository;

    public Optional<CitizenProfile> getProfileByCitizenId(String citizenId) {
        return repository.findByCitizenId(citizenId);
    }

    public CitizenProfile createProfile(CitizenProfile profile) {
        return repository.save(profile);
    }

    public CitizenProfile updateProfile(String citizenId, CitizenProfile updated) {
        CitizenProfile profile = repository.findByCitizenId(citizenId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        profile.setUserName(updated.getUserName());
        profile.setEmail(updated.getEmail());
        profile.setGender(updated.getGender());
        profile.setAddress(updated.getAddress());
        profile.setCity(updated.getCity());
        profile.setState(updated.getState());
        profile.setPincode(updated.getPincode());
        profile.setDob(updated.getDob());
        profile.setProfileUrl(updated.getProfileUrl());

        return repository.save(profile);
    }
}