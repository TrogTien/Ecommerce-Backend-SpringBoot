package com.example.shopapp.controllers;

import com.example.shopapp.dtos.CategoryDTO;
import com.example.shopapp.models.Category;
import com.example.shopapp.services.interfaces.ICategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.base.path}/categories")
public class CategoryController {
    private final ICategoryService categoryService;
    private final MessageSource messageSource;

    @GetMapping("")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @PostMapping("")
    public ResponseEntity<?> addCategory(
            @Valid @RequestBody CategoryDTO categoryDTO,
            BindingResult bindingResult
    ) {
        // check DTO Valid
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(String.valueOf(errors));
        }

        Category newCategory = categoryService.createCategory(categoryDTO);
        return ResponseEntity.ok(newCategory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(
            @PathVariable Long id,
            @RequestBody @Valid CategoryDTO categoryDTO,
            BindingResult bindingResult


    ) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(String.valueOf(errors));
        }

        Category updatedCategory = categoryService.updateCategory(categoryDTO, id);

        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(
            @PathVariable Long id
    ) {
        try {

            categoryService.deleteCategory(id);
            Locale locale = LocaleContextHolder.getLocale();
            return ResponseEntity.ok(Collections.singletonMap(
                    "message",
                    messageSource.getMessage("category.delete.delete_successfully", null, locale)
            ));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
