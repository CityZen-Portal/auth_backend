package com.cityzen.auth.repository;

import com.cityzen.auth.entity.CitizenProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CitizenProfileRepository extends JpaRepository<CitizenProfile, Long> {
    Optional<CitizenProfile> findByCitizenId(String citizenId);
    Optional<CitizenProfile> findByEmail(String email);
}