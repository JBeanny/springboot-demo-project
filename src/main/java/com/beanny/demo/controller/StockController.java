package com.beanny.demo.controller;

import com.beanny.demo.dto.stock.StockDto;
import com.beanny.demo.model.BaseResponseModel;
import com.beanny.demo.model.BaseResponseWithDataModel;
import com.beanny.demo.dto.stock.UpdateStockDto;
import com.beanny.demo.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/stocks")
public class StockController {
    @Autowired
    private StockService stockService;
    
    @GetMapping
    public ResponseEntity<BaseResponseWithDataModel> listStocks() {
        return stockService.listStocks();
    }
    
    @GetMapping("{id}")
    public ResponseEntity<BaseResponseWithDataModel> getStock(@PathVariable("id") Long stockId) {
        return stockService.getStock(stockId);
    }
    
    @PostMapping
    public ResponseEntity<BaseResponseModel> createStock(@RequestBody StockDto payload) {
        return stockService.createStock(payload);
    }
    
    @PatchMapping("{id}")
    public ResponseEntity<BaseResponseModel> adjustQuantity(@PathVariable("id") Long stockId,@RequestBody UpdateStockDto payload) {
        return stockService.adjustQuantity(stockId,payload);
    }
    
    @DeleteMapping("{id}")
    public ResponseEntity<BaseResponseModel> deleteStock(@PathVariable("id") Long stockId) {
        return stockService.deleteStock(stockId);
    }
}
