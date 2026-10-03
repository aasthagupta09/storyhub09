package com.storyhub.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/** A registered person: can write stories and engage with others'. Table: users */
@Entity @Table(name = "users")
@Getter @Setter @NoArgsConstructor
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 40)
    private String username;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String passwordHash;          // BCrypt hash, never the plain password

    @Column(length = 500)
    private String bio;

    private LocalDateTime createdAt = LocalDateTime.now();
}
