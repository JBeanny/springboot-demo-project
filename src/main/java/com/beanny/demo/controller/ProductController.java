package com.beanny.demo.controller;

import com.beanny.demo.dto.product.ProductDto;
import com.beanny.demo.model.BaseResponseModel;
import com.beanny.demo.model.BaseResponseWithDataModel;
import com.beanny.demo.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    @Autowired
    private ProductService productService;
    
    @GetMapping
    public ResponseEntity<BaseResponseWithDataModel> listProducts() {
        return productService.listProducts();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponseWithDataModel> getProduct(@PathVariable("id") Long productId) {
        return productService.getProduct(productId);
    }
    
    @GetMapping("/search")
    public ResponseEntity<BaseResponseWithDataModel> searchProductsByFilters(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "minPrice", required = false) Double minPrice,
            @RequestParam(value = "maxPrice", required = false) Double maxPrice
    ) {
        return productService.searchProducts(name,minPrice,maxPrice);
    }
    
    @PostMapping
    public ResponseEntity<BaseResponseModel> createProduct(@Valid @RequestBody ProductDto payload) {
        return productService.createProduct(payload);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<BaseResponseModel> updateProduct(@PathVariable("id") Long productId,@RequestBody ProductDto payload) {
        return productService.updateProduct(productId,payload);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponseModel> deleteProduct(@PathVariable("id") Long productId) {
        return productService.deleteProduct(productId);
    }
}
