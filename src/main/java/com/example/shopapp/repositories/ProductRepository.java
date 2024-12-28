package com.example.shopapp.repositories;

import com.example.shopapp.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByName(String name);


    @Query("SELECT p FROM Product p WHERE (:categoryId IS NULL OR :categoryId = 0 OR p.category.id  = :categoryId)" +
            "AND (:search IS NULL OR :search = '' OR p.name LIKE %:search% OR p.description LIKE %:search%)" +
            "AND p.isActive = false ")
    Page<Product> searchDeletedProducts(
            @Param("search") String search,
            @Param("categoryId") Long categoryId,
            Pageable pageable
    );

    // Truy vấn chỉ lấy các sản phẩm chưa bị xóa


    @Query("SELECT p FROM Product p WHERE (:categoryId IS NULL OR :categoryId = 0 OR p.category.id  = :categoryId)" +
            "AND (:search IS NULL OR :search = '' OR p.name LIKE %:search% OR p.description LIKE %:search%)" +
            "AND p.isActive = true")
    Page<Product> searchActiveProducts(
            @Param("search") String search,
            @Param("categoryId") Long categoryId,
            Pageable pageable
    );


}
