package com.example.shopapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CartItemDTO {
    @JsonProperty("product_id")
    private Long productId;

    private Integer quantity;
}
