package com.example.shopapp.services;

import com.example.shopapp.dtos.ProductDTO;
import com.example.shopapp.dtos.ProductImageDTO;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.exceptions.InvalidParamException;
import com.example.shopapp.models.Category;
import com.example.shopapp.models.Product;
import com.example.shopapp.models.ProductImage;
import com.example.shopapp.repositories.CategoryRepository;
import com.example.shopapp.repositories.ProductImageRepository;
import com.example.shopapp.repositories.ProductRepository;
import com.example.shopapp.services.interfaces.IProductService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;

    @Override
    @Transactional
    public Product createProduct(ProductDTO productDTO, String thumbnail) throws DataNotFoundException {
        Category existingCategory = categoryRepository
                .findById(productDTO.getCategoryId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find category with id: " + productDTO.getCategoryId()));

        Product newProduct = Product.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .thumbnail(thumbnail)
                .description(productDTO.getDescription())
                .category(existingCategory)
                .build();

        return productRepository.save(newProduct);
    }

    @Override
    public Product getProductById(long productId) throws Exception {
        return productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException("Cannot find product with id = " + productId));
    }

    @Override
    public List<Product> getProductsByIds(List<Long> ids)  {

        return productRepository.findAllById(ids);
    }


    @Override
    public Page<Product> getAllDeletedProducts(String search, Long categoryId, PageRequest pageRequest) {
        return productRepository.searchDeletedProducts(search, categoryId, pageRequest);
    }

    @Override
    public Page<Product> getAllActiveProducts(String search, Long categoryId, PageRequest pageRequest) {
        return productRepository.searchActiveProducts(search, categoryId, pageRequest);
    }

    @Override
    @Transactional
    public Product updateProduct(long id, ProductDTO productDTO, String thumbnail) throws Exception {
        Product existingProduct = getProductById(id);

        if (existingProduct != null) {
            Category existingCategory = categoryRepository
                    .findById(productDTO.getCategoryId())
                    .orElseThrow(() -> new DataNotFoundException("Cannot find category with id: " + productDTO.getCategoryId()));

            existingProduct.setName(productDTO.getName());
            existingProduct.setPrice(productDTO.getPrice());
            existingProduct.setDescription(productDTO.getDescription());
            existingProduct.setCategory(existingCategory);
            if (!thumbnail.isEmpty()) {
                existingProduct.setThumbnail(thumbnail);
            }
            return productRepository.save(existingProduct);
        }
        return null;
    }

    @Override
    @Transactional
    public void softDeleteProduct(long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        product.setIsActive(false); // Đánh dấu sản phẩm đã bị xóa
        productRepository.save(product);

    }

    @Override
    @Transactional
    public void restoreProduct(long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        product.setIsActive(true); // Khoi phuc sp bi xoa
        productRepository.save(product);
    }

    @Override
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    @Override
    @Transactional
    public ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO) throws DataNotFoundException, InvalidParamException {
        Product existingProduct = productRepository
                .findById(productId)
                .orElseThrow(() -> new DataNotFoundException(
                        "Cannot find product with id = " + productId
                ));

        ProductImage newProductImage = ProductImage.builder()
                .product(existingProduct)
                .imageUrl(productImageDTO.getImageUrl())
                .build();
        // ko cho insert  5 Image cho 1 Product
        int size = productImageRepository.findByProductId(productId).size();
        if (size > ProductImage.MAX_SIZE_IMAGE) {
            throw new InvalidParamException("Number of images must be < " + ProductImage.MAX_SIZE_IMAGE);
        }

        return productImageRepository.save(newProductImage);
    }
}
