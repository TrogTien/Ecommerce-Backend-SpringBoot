package com.example.shopapp.responses;

import com.example.shopapp.models.Product;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductListResponse {
    private List<Product> products;
    private int totalPages;
}
