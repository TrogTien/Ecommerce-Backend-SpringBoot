package com.example.shopapp.services.interfaces;

import com.example.shopapp.exceptions.InvalidParamException;
import com.example.shopapp.models.ProductImage;

import java.util.List;

public interface IProductImageService {
    List<ProductImage> getImagesByProductId(long productId);

    void deleteImageByProductId(long productId);

    void deleteImageByImageUrl(String imageUrl);

    List<ProductImage> updateProductImages(Long productId, List<ProductImage> images) throws InvalidParamException;
}
