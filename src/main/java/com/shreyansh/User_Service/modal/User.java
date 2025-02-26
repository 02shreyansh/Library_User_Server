package com.shreyansh.User_Service.modal;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false, unique = true)
    private  String id;

    @Column(name = "full_name", nullable = false, length = 255)
    private  String full_name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "university_id", nullable = false, unique = true)
    private Long university_id;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private UserStatus status=UserStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole userRole=UserRole.USER;

    @Column(name = "last_activity_date", nullable = false)
    private Instant lastActivityDate = Instant.now();

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();
}
