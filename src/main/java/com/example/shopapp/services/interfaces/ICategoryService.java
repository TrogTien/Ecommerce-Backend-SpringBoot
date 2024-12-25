package com.example.shopapp.services.interfaces;

import com.example.shopapp.dtos.CategoryDTO;
import com.example.shopapp.models.Category;

import java.util.List;

public interface ICategoryService {
    Category createCategory(CategoryDTO categoryDTO);

    Category getCategory(long id);

    List<Category> getAllCategories();

    Category updateCategory(CategoryDTO categoryDTO, long id);

    void deleteCategory(long id);
}
