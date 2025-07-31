package com.cityzen.auth.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
public class AadhaarRegistry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    @NotBlank(message = "Aadhaar number cannot be blank")
    private String aadhaarNumber;

    public AadhaarRegistry() {
    }

    @Override
    public String toString() {
        return "AadhaarRegistry{" +
                "id=" + id +
                ", aadhaarNumber='" + aadhaarNumber + '\'' +
                '}';
    }
}