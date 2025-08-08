package com.beanny.demo.service;

import com.beanny.demo.dto.stock.StockDto;
import com.beanny.demo.entity.Product;
import com.beanny.demo.entity.Stock;
import com.beanny.demo.exception.model.ResourceNotFoundException;
import com.beanny.demo.mapper.StockMapper;
import com.beanny.demo.model.BaseResponseModel;
import com.beanny.demo.model.BaseResponseWithDataModel;
import com.beanny.demo.dto.stock.UpdateStockDto;
import com.beanny.demo.repository.ProductRepository;
import com.beanny.demo.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StockService {
    @Autowired
    private StockRepository stockRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private StockMapper mapper;
    
    public ResponseEntity<BaseResponseWithDataModel> listStocks() {
        List<Stock> stocks = stockRepository.findAll();
        
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseWithDataModel("success","successfully retrieved stocks",mapper.toDtoList(stocks)));
    }
    
    public ResponseEntity<BaseResponseWithDataModel> getStock(Long stockId) {
        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new ResourceNotFoundException("stock not found with id: " + stockId));
        
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseWithDataModel("success","stock found",mapper.toDto(stock)));
    }
    
    public ResponseEntity<BaseResponseModel> createStock(StockDto stock) {
        Product existingProduct = productRepository.findById(stock.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("product not found: " + stock.getProductId()));
        
        Stock stockEntity = mapper.toEntity(stock,existingProduct);
        
        stockRepository.save(stockEntity);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new BaseResponseModel("success","successfully created stock"));
    }
    
    public ResponseEntity<BaseResponseModel> adjustQuantity(Long stockId, UpdateStockDto updateStock) {
        Stock existingStock = stockRepository.findById(stockId)
                .orElseThrow(() -> new ResourceNotFoundException("stock not found with id: " + stockId));
        
        if(updateStock.getOperationType() == 1) { // add
            int newQty = existingStock.getQuantity() + updateStock.getQuantity();
            
            existingStock.setQuantity(newQty);
        } else if(updateStock.getOperationType() == 2) { // remove
            // when remove amount > existing amount
            if(existingStock.getQuantity() < updateStock.getQuantity()) {
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                        .body(new BaseResponseModel("fail","quantity to remove can not be exceeded than existing stock: " + existingStock.getQuantity()));
            }
            
            int newQty = existingStock.getQuantity() - updateStock.getQuantity();
            
            existingStock.setQuantity(newQty);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new BaseResponseModel("fail","invalid operation type"));
        }
        
        stockRepository.save(existingStock);
        
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseModel("success","successfully adjusted stock quantity"));
    }
    
    public ResponseEntity<BaseResponseModel> deleteStock(Long stockId) {
        if(!stockRepository.existsById(stockId)) {
            throw new ResourceNotFoundException("stock is not found: " + stockId);
        }
        
        stockRepository.deleteById(stockId);
        
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseModel("success","successfully deleted stock"));
    }
}
