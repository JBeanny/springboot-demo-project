package com.beanny.demo.repository;

import com.beanny.demo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    @Query("SELECT p FROM Product p WHERE " +
            "(:name IS NULL OR LOWER(p.productName) LIKE %:name%) AND" +
            "(:minPrice IS NULL OR p.price >= :minPrice) AND" +
            "(:maxPrice IS NULL OR p.price <= :maxPrice)"
    )
    List<Product> findProductsWithFilters(
            @Param("name") String name,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice
    );
}
