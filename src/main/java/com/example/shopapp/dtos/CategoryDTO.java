package com.example.shopapp.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Data
public class CategoryDTO {
    @NotEmpty(message = "Category cannot empty")
    private String name;
}
