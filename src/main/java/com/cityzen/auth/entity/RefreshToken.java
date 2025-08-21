package com.cityzen.auth.entity;

import jakarta.persistence.*;
import java.time.Instant;
import lombok.Data;

@Data
@Table(name = "refresh_token")

@Entity
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;
    @Column(nullable = false)
    private Instant expiryDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}