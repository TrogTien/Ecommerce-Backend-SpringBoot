package com.example.shopapp.services;

import com.example.shopapp.dtos.CategoryDTO;
import com.example.shopapp.models.Category;
import com.example.shopapp.repositories.CategoryRepository;
import com.example.shopapp.repositories.ProductRepository;
import com.example.shopapp.services.interfaces.ICategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public Category createCategory(CategoryDTO categoryDTO) {
        Category newCategory = Category.
                builder().
                name(categoryDTO.getName()).
                build();
        return categoryRepository.save(newCategory);
    }

    @Override
    public Category getCategory(long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    @Transactional
    public Category updateCategory(CategoryDTO categoryDTO, long id) {
        Category existingCategory = getCategory(id);
        existingCategory.setName(categoryDTO.getName());
        categoryRepository.save(existingCategory);
        return existingCategory;
    }

    @Override
    @Transactional
    public void deleteCategory(long categoryId) {
        // xoa cung
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isPresent()) {
            productRepository.deleteByCategoryId(categoryId);
            categoryRepository.deleteById(categoryId);
        }
    }
}
