package com.example.shopapp.responses;

import com.example.shopapp.models.OrderDetail;
import com.example.shopapp.models.Product;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailResponse {
    private Long id;

    @JsonProperty("order_id")
    private Long orderId;


    private Product product;

    private Float price;

    @JsonProperty("number_of_products")
    private Integer numberOfProducts;

    @JsonProperty("total_money")
    private Float totalMoney;

    private String color;

    public static OrderDetailResponse fromOrderDetail(OrderDetail orderDetail) {
        return OrderDetailResponse.builder()
                .id(orderDetail.getId())
                .orderId(orderDetail.getOrder().getId())
                .product(orderDetail.getProduct())
                .price(orderDetail.getPrice())
                .numberOfProducts(orderDetail.getNumberOfProducts())
                .totalMoney(orderDetail.getTotalMoney())
                .color(orderDetail.getColor())
                .build();
    }
}
