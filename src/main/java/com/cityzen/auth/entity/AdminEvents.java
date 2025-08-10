package com.cityzen.auth.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Collection;
import lombok.Data;

@Data
@Entity
@Table(name = "admin_events")
public class AdminEvents {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String adminId;
    private LocalDate date;
    private String startTime;
    private String endTime;
    private String location;
    private String description;

    public AdminEvents() {}

    public AdminEvents(String title, String adminId, LocalDate date) {
        this.title = title;
        this.adminId = adminId;
        this.date = date;
    }
}