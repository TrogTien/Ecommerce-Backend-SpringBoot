package com.example.shopapp.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Table(name = "orders")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "full_name", length = 100)
    private String fullName;

    @Column(length = 100)
    private String email;

    @Column(name = "phone_number", length = 100)
    private String phoneNumber;

    @Column(length = 100)
    private String address;

    @Column(length = 100)
    private String note;

    @Column(name = "order_date", length = 100)
    private LocalDate orderDate;

    @Column( length = 100)
    private String status;

    @Column(name = "total_money", length = 100)
    private Float totalMoney;

    @Column(name = "shipping_method", length = 100)
    private String shippingMethod;

    @Column(name = "shipping_address", length = 100)
    private String shippingAddress;

    @Column(name = "shipping_date", length = 100)
    private LocalDate shippingDate;

    @Column(name = "tracking_number", length = 100)
    private String trackingNumber;

    @Column(name = "payment_method", length = 100)
    private String paymentMethod;

    @Column(length = 100)
    private Boolean active;

    @Column(name = "tax", length = 100)
    private Float tax;

    @Column(name = "shipping_cost", length = 100)
    private Float shippingCost;

    @Column(name = "sub_total", length = 100)
    private Float subTotal;
}
