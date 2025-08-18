package com.beanny.demo.controller;

import com.beanny.demo.dto.product.ProductDto;
import com.beanny.demo.dto.product.ProductResponseDto;
import com.beanny.demo.model.BaseResponseModel;
import com.beanny.demo.model.BaseResponseWithDataModel;
import com.beanny.demo.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    @Autowired
    private ProductService productService;
    
    @GetMapping
    public ResponseEntity<BaseResponseWithDataModel> listProducts() {
        List<ProductResponseDto> products = productService.listProducts();
        
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseWithDataModel("success","successfully retrieved products",products));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponseWithDataModel> getProduct(@PathVariable("id") Long productId) {
        ProductResponseDto product = productService.getProduct(productId);
        
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseWithDataModel("success","product found",product));
    }
    
    @GetMapping("/search")
    public ResponseEntity<BaseResponseWithDataModel> searchProductsByFilters(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "minPrice", required = false) Double minPrice,
            @RequestParam(value = "maxPrice", required = false) Double maxPrice
    ) {
        List<ProductResponseDto> products = productService.searchProducts(name,minPrice,maxPrice);
        
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseWithDataModel("success","successfully retrieved products with filters",products));
    }
    
    @PostMapping
    public ResponseEntity<BaseResponseModel> createProduct(@Valid @RequestBody ProductDto payload) {
        productService.createProduct(payload);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BaseResponseModel("success","successfully created product"));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<BaseResponseModel> updateProduct(@PathVariable("id") Long productId,@RequestBody ProductDto payload) {
        productService.updateProduct(productId,payload);
        
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseModel("success","successfully updated product"));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponseModel> deleteProduct(@PathVariable("id") Long productId) {
        productService.deleteProduct(productId);
        
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseModel("success","successfully deleted product id: " + productId));
    }
}
