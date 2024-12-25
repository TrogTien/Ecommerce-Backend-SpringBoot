package com.example.shopapp.services;

import com.example.shopapp.exceptions.InvalidParamException;
import com.example.shopapp.models.Product;
import com.example.shopapp.models.ProductImage;
import com.example.shopapp.repositories.ProductImageRepository;
import com.example.shopapp.services.interfaces.IProductImageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductImageService implements IProductImageService {

    private final ProductImageRepository productImageRepository;

    @Override
    public List<ProductImage> getImagesByProductId(long productId) {
        return productImageRepository.findByProductId(productId);
    }

    @Override
    @Transactional
    public void deleteImageByProductId(long productId) {
        this.productImageRepository.deleteByProductId(productId);
    }

    @Override
    @Transactional
    public List<ProductImage> updateProductImages(Long productId, List<ProductImage> images) throws InvalidParamException {
        int size = productImageRepository.findByProductId(productId).size();
        if (size + images.size() > ProductImage.MAX_SIZE_IMAGE) {
            throw new InvalidParamException("Number of images must be < " + ProductImage.MAX_SIZE_IMAGE);
        }
        return productImageRepository.saveAll(images);
    }

    @Override
    @Transactional
    public void deleteImageByImageUrl(String imageUrl) {
        Optional<ProductImage> imageOptional = productImageRepository.findByImageUrl(imageUrl);
        imageOptional.ifPresent(productImageRepository::delete);
    }
}
