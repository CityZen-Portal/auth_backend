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

    private String token;
    private Instant expiryDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}