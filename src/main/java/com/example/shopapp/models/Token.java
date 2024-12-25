package com.example.shopapp.models;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "tokens")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column( length = 255)
    private String token;

    @Column(name = "token_type", length = 255)
    private String tokenType;

    @Column(name = "expiration_date", length = 255)
    private String expirationDate;

    private Boolean revoked;
    private Boolean expired;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
