package com.cityzen.auth.repository;

import com.cityzen.auth.entity.AdminProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminProfileRepository extends JpaRepository<AdminProfile, Long> {
    Optional<AdminProfile> findByAdminId(String adminId);
    Optional<AdminProfile> findByEmail(String email);
}