package com.example.shopapp.responses;

import com.example.shopapp.models.Product;
import com.example.shopapp.models.ProductImage;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse extends BaseResponse {
    private Long id;
    private String name;

    private Float price;

    private String description;
    private String thumbnail;

    @JsonProperty("category_id")
    private Long categoryId;

    private List<String> imageUrls;

    public static ProductResponse fromProduct(Product product) {
        ProductResponse productResponse = ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .thumbnail(product.getThumbnail())
                .description(product.getDescription())
                .categoryId(product.getCategory().getId())
                .build();

        productResponse.setCreatedAt(product.getCreatedAt());
        productResponse.setUpdatedAt(product.getUpdatedAt());

        return productResponse;

    }
}
