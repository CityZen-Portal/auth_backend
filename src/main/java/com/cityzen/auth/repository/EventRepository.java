package com.cityzen.auth.repository;

import com.cityzen.auth.entity.AdminEvents;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<AdminEvents, Long> {
    List<AdminEvents> findByAdminId(String adminId);
}