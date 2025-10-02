package com.beanny.demo.service;

import com.beanny.demo.dto.base.PaginatedResponse;
import com.beanny.demo.dto.product.ProductDto;
import com.beanny.demo.dto.product.ProductResponseDto;
import com.beanny.demo.entity.Product;
import com.beanny.demo.mapper.ProductMapper;
import com.beanny.demo.exception.model.DuplicateResourceException;
import com.beanny.demo.exception.model.ResourceNotFoundException;
import com.beanny.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private ProductMapper mapper;
    
    public PaginatedResponse listProductsWithPagination(Pageable pageable) {
        Page<Product> productPages = productRepository.findAll(pageable);
        Page<ProductResponseDto> productPagesDto = productPages.map(product -> mapper.toDto(product));
        
        return PaginatedResponse.from(productPagesDto);
    }
    
    public List<ProductResponseDto> listProducts() {
        List<Product> products = productRepository.findAll();
        
        return mapper.toDtoList(products);
    }
    
    public ProductResponseDto getProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("product not found with id : " + productId));
        
        return mapper.toDto(product);
    }
    
    public void createProduct(ProductDto product) {
        // validate if product is already existed
        if(productRepository.existsByProductName(product.getName())) {
            throw new DuplicateResourceException("product is already existed");
        }
        
        Product productEntity = mapper.toEntity(product);
        
        productRepository.save(productEntity);
    }
    
    public void updateProduct(Long productId,ProductDto product) {
        Product existing = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("product not found with id : " + productId));
        
        existing.setProductName(product.getName());
        existing.setDescription(product.getDescription());
        existing.setPrice(product.getPrice());
        
        productRepository.save(existing);
    }
    
    public void deleteProduct(Long productId) {
        if(!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("product not found with id : " + productId);
        }
        
        productRepository.deleteById(productId);
    }
    
    public List<ProductResponseDto> searchProducts(String name, Double minPrice, Double maxPrice) {
        String formattedName = name != null ?
                name.toLowerCase()
                : null;
        
        List<Product> products = productRepository.findProductsWithFilters(formattedName,minPrice,maxPrice);
        
        return mapper.toDtoList(products);
    }
}
