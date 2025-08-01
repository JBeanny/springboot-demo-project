package com.beanny.demo.service;

import com.beanny.demo.dto.product.ProductDto;
import com.beanny.demo.entity.Product;
import com.beanny.demo.mapper.ProductMapper;
import com.beanny.demo.model.BaseResponseModel;
import com.beanny.demo.model.BaseResponseWithDataModel;
import com.beanny.demo.exception.model.DuplicateResourceException;
import com.beanny.demo.exception.model.ResourceNotFoundException;
import com.beanny.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private ProductMapper mapper;
    
    public ResponseEntity<BaseResponseWithDataModel> listProducts() {
        List<Product> products = productRepository.findAll();
        
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseWithDataModel("success","successfully retrieved products",mapper.toDtoList(products)));
    }
    
    public ResponseEntity<BaseResponseWithDataModel> getProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("product not found with id : " + productId));
        
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseWithDataModel("success","product found",product));
    }
    
    public ResponseEntity<BaseResponseModel> createProduct(ProductDto product) {
        // validate if product is already existed
        if(productRepository.existsByProductName(product.getName())) {
            throw new DuplicateResourceException("product is already existed");
        }
        
        Product productEntity = mapper.toEntity(product);
        
        productRepository.save(productEntity);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BaseResponseModel("success","successfully created product"));
    }
    
    public ResponseEntity<BaseResponseModel> updateProduct(Long productId,ProductDto product) {
        Product existing = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("product not found with id : " + productId));
        
        existing.setProductName(product.getName());
        existing.setDescription(product.getDescription());
        existing.setPrice(product.getPrice());
        
        productRepository.save(existing);
        
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseModel("success","successfully updated product"));
    }
    
    public ResponseEntity<BaseResponseModel> deleteProduct(Long productId) {
        if(!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("product not found with id : " + productId);
        }
        
        productRepository.deleteById(productId);
        
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseModel("success","successfully deleted product id: " + productId));
    }
    
    public ResponseEntity<BaseResponseWithDataModel> searchProducts(String name, Double minPrice, Double maxPrice) {
        String formattedName = name != null ?
                name.toLowerCase()
                : null;
        
        List<Product> product = productRepository.findProductsWithFilters(formattedName,minPrice,maxPrice);
        
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseWithDataModel("success","successfully retrieved products with filters",product));
    }
}
