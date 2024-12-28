package com.example.shopapp.services.interfaces;

import com.example.shopapp.dtos.ProductDTO;
import com.example.shopapp.dtos.ProductImageDTO;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.exceptions.InvalidParamException;
import com.example.shopapp.models.Product;
import com.example.shopapp.models.ProductImage;
import com.example.shopapp.responses.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface IProductService {
    Product createProduct(ProductDTO productDTO, String thumbnail) throws DataNotFoundException;

    Product getProductById(long id) throws Exception;

    List<Product> getProductsByIds(List<Long> ids) ;

    Page<Product> getAllDeletedProducts(String search, Long categoryId, PageRequest pageRequest);

    Page<Product> getAllActiveProducts(String search, Long categoryId, PageRequest pageRequest);

    Product updateProduct(long id, ProductDTO productDTO, String thumbnail) throws Exception;

    void softDeleteProduct(long id);

    void restoreProduct(long id);

    boolean existsByName(String name);

    ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO) throws DataNotFoundException, InvalidParamException;




}
