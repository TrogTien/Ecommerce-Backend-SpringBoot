package com.example.shopapp.models;


import jakarta.persistence.*;
import lombok.*;

@Table(name = "categories")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;
}
