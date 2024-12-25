package com.example.shopapp.controllers;

import com.example.shopapp.components.ImageFileUtil;
import com.example.shopapp.dtos.ProductImageDTO;
import com.example.shopapp.models.Product;
import com.example.shopapp.models.ProductImage;
import com.example.shopapp.services.interfaces.IProductImageService;
import com.example.shopapp.services.interfaces.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.base.path}/product-images")
public class ProductImageController {
    private final IProductService productService;
    private final IProductImageService productImageService;
    private final ImageFileUtil imageFileUtil;


//Uploads
    @PostMapping(value = "uploads/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages(
            @PathVariable("productId") Long productId,
            @ModelAttribute("files") List<MultipartFile> files
    ) {
        try {
            Product existingProduct = productService.getProductById(productId);

            if (files == null || files.isEmpty()) {
                files = new ArrayList<>();
            }

            if (files.size() > ProductImage.MAX_SIZE_IMAGE) {
                return ResponseEntity.badRequest().body("You can only upload images with at most 5 images");
            }

            List<ProductImage> productImages = new ArrayList<>();

            for (MultipartFile file : files) {
                // Check file size
                long fileSize = file.getSize();
                if (fileSize == 0) continue;

                if (fileSize > 10 * 1024 * 1024) {
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                            .body("File is too large, Maximum size is 10MB");
                }

                // Check file type
                String contentType = file.getContentType();
                if (contentType == null || !contentType.startsWith("image")) {
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                            .body("File must be an image");
                }

                // Save file thumbnail
                String fileName = imageFileUtil.storeFile(file);

                // Create ProductImage object
                ProductImage productImage = new ProductImage();
                productImage.setProduct(existingProduct);
                productImage.setImageUrl(fileName);

                productImages.add(productImage);
            }

            // Save all images in one SQL operation
            List<ProductImage> result = productImageService.updateProductImages(productId, productImages);


            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



    @DeleteMapping("/{imgUrl}")
    public ResponseEntity<?> deleteProductByImgUrl(@PathVariable("imgUrl") String imgUrl) {
        try {
            productImageService.deleteImageByImageUrl(imgUrl);
            Map<String, String> response = new HashMap<>();
            response.put("message", String.format("Product Image with name %s was deleted", imgUrl));

            return ResponseEntity.ok(response);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
