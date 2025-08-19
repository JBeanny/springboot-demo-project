package com.beanny.demo.controller;

import com.beanny.demo.dto.base.Response;
import com.beanny.demo.dto.stock.StockDto;
import com.beanny.demo.dto.stock.StockResponseDto;
import com.beanny.demo.model.BaseResponseModel;
import com.beanny.demo.model.BaseResponseWithDataModel;
import com.beanny.demo.dto.stock.UpdateStockDto;
import com.beanny.demo.service.StockService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/stocks")
public class StockController {
    @Autowired
    private StockService stockService;
    
    @GetMapping
    public ResponseEntity<Response> listStocks() {
        List<StockResponseDto> stocks = stockService.listStocks();
        
        return ResponseEntity.status(HttpStatus.OK)
                .body(Response.success("200","success","successfully retrieved stocks",stocks));
    }
    
    @GetMapping("{id}")
    public ResponseEntity<Response> getStock(@PathVariable("id") Long stockId) {
        StockResponseDto stock = stockService.getStock(stockId);
        
        return ResponseEntity.status(HttpStatus.OK)
                .body(Response.success("200","success","stock found",stock));
    }
    
    @PostMapping
    public ResponseEntity<Response> createStock(@Valid @RequestBody StockDto payload) {
        stockService.createStock(payload);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Response.success("201","success","successfully created stock"));
    }
    
    @PatchMapping("{id}")
    public ResponseEntity<Response> adjustQuantity(@PathVariable("id") Long stockId,@Valid @RequestBody UpdateStockDto payload) {
        stockService.adjustQuantity(stockId,payload);
        
        return ResponseEntity.status(HttpStatus.OK)
                .body(Response.success("200","success","successfully adjusted stock quantity"));
    }
    
    @DeleteMapping("{id}")
    public ResponseEntity<Response> deleteStock(@PathVariable("id") Long stockId) {
        stockService.deleteStock(stockId);
        
        return ResponseEntity.status(HttpStatus.OK)
                .body(Response.success("200","success","successfully deleted stock"));
    }
}
