package com.example.shopapp.controllers;

import com.example.shopapp.components.ImageFileUtil;
import com.example.shopapp.dtos.ProductDTO;
import com.example.shopapp.models.Product;
import com.example.shopapp.models.ProductImage;
import com.example.shopapp.responses.ProductListResponse;
import com.example.shopapp.responses.ProductResponse;
import com.example.shopapp.services.interfaces.IProductImageService;
import com.example.shopapp.services.interfaces.IProductService;
//import com.github.javafaker.Faker;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.base.path}/products")
public class ProductController {
    private final IProductService productService;
    private final IProductImageService productImageService;
    private final ImageFileUtil imageFileUtil;

    @GetMapping()
    public ResponseEntity<?> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int limit,
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0", name = "category_id") Long categoryId
    ) {

        try {
            PageRequest pageRequest = PageRequest.of(
                    page,
                    limit,
                    Sort.by("createdAt").ascending()
            );
            Page<Product> productPage = productService.getAllProducts(search, categoryId, pageRequest);
            // tong so pages
            int totalPage  = productPage.getTotalPages();
            List<Product> products = productPage.getContent();

            return ResponseEntity.ok(ProductListResponse.builder()
                    .products(products)
                    .totalPages(totalPage)
                    .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }


    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") Long productId) {
        try {
            Product existingProduct = productService.getProductById(productId);

            List<ProductImage> productImages = productImageService.getImagesByProductId(productId);
            List<String> imageUrls = productImages.stream()
                    .map(ProductImage::getImageUrl)
                    .toList();

            ProductResponse response = ProductResponse.fromProduct(existingProduct);
            response.setImageUrls(imageUrls);
            return ResponseEntity.ok(response);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
//List Product by List id
    @GetMapping("by-ids")
    public ResponseEntity<?> getProductsByIds(@RequestParam(name = "ids") List<Long> productIds) {
        List<Product> productResponseList = productService.getProductsByIds(productIds);
        
        return ResponseEntity.ok(productResponseList);
    }


//    Add Product (New Api with file thumbnail)
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addProduct(
            @Valid @ModelAttribute ProductDTO productDTO,
            BindingResult bindingResult
    ) {
        try {
            if (bindingResult.hasErrors()) {
                List<String> errors = bindingResult.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(String.valueOf(errors));
            }

            String fileName = imageFileUtil.validateAndStoreThumbnail(productDTO.getThumbnail());

            Product newProduct = productService.createProduct(productDTO, fileName);
            return ResponseEntity.ok(newProduct);


        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProduct(
        @PathVariable("id") Long productId,
        @Valid @ModelAttribute ProductDTO productDTO,
        BindingResult bindingResult
    ) {
        try {
            if (bindingResult.hasErrors()) {
                List<String> errors = bindingResult.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(String.valueOf(errors));
            }

            String fileName = imageFileUtil.validateAndStoreThumbnail(productDTO.getThumbnail());

            Product updatedProduct = productService.updateProduct(productId, productDTO, fileName);
            return ResponseEntity.ok(updatedProduct);

        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProductById(@PathVariable("id") Long productId) {
        try {
            productService.deleteProduct(productId);
            return ResponseEntity.ok(String.format("Product with id %s was deleted", productId));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

//    @PostMapping("/generateFakeProducts")
//    public ResponseEntity<String> generateFakeProducts() {
//        Faker faker = new Faker();
//
//        for (int i = 0; i < 1_000; i++) {
//            String productName = faker.commerce().productName();
//            if (productService.existsByName(productName)) {
//                continue;
//            }
//
//            ProductDTO productDTO = ProductDTO.builder()
//                    .name(productName)
//                    .price((float)faker.number().numberBetween(10, 9_000_000))
//                    .description(faker.lorem().sentence())
//                    .thumbnail("")
//                    .categoryId((long)faker.number().numberBetween(1, 3))
//                    .build();
//
//            try {
//                productService.createProduct(productDTO);
//            } catch (DataNotFoundException e) {
//                return ResponseEntity.badRequest().body(e.getMessage());
//            }
//        }
//
//        return ResponseEntity.ok("Fake products generated successfully");
//    }



}
