package com.example.shopapp.models;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "social_accounts")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SocialAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String provider;

    @Column(name = "provider_id", nullable = false, length = 50)
    private String providerId;

    @Column(length = 150)
    private String name;

    @Column(length = 150)
    private String email;

}
