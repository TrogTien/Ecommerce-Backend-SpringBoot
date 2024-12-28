package com.example.shopapp.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Table(name = "products")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    @Column(nullable = false, length = 350)
    private String name;

    private Float price;

    @Column(length = 300)
    private String thumbnail;

    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}
