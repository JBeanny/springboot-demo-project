package com.beanny.demo.repository;

import com.beanny.demo.entity.Stock;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockRepository extends JpaRepository<Stock,Long> {
    // SELECT * FROM stocks WHERE product_id IN (1,3) ORDER BY <sorting_param>
    List<Stock> findByProductIdIn(List<Long> productIds, Sort createdAt);
}
