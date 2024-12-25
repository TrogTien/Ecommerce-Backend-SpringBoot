package com.example.shopapp.models;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "roles")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    public static String ADMIN = "ADMIN";
    public static String USER = "USER";
}
