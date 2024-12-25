package com.example.shopapp.models;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "product_images")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductImage {
    public static final int MAX_SIZE_IMAGE = 5;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "image_url", length = 300)
    private String imageUrl;

}
