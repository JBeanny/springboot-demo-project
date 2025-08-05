package com.beanny.demo.controller;

import com.beanny.demo.dto.supplier.SupplierDto;
import com.beanny.demo.dto.supplier.UpdateSupplierDto;
import com.beanny.demo.model.BaseResponseModel;
import com.beanny.demo.model.BaseResponseWithDataModel;
import com.beanny.demo.service.SupplierService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/suppliers")
public class SupplierController {
    @Autowired
    private SupplierService supplierService;
    
    @GetMapping
    public ResponseEntity<BaseResponseWithDataModel> listSuppliers() {
        return supplierService.listSuppliers();
    }
    
    @PostMapping
    public ResponseEntity<BaseResponseModel> createSupplier(@Valid @RequestBody SupplierDto payload) {
        return supplierService.createSupplier(payload);
    }
    
    @PutMapping("{supplier_id}")
    public ResponseEntity<BaseResponseModel> updateSupplier(
                @PathVariable("supplier_id") Long supplierId,
                @Valid @RequestBody UpdateSupplierDto payload
            ) {
        return supplierService.updateSupplier(supplierId,payload);
    }
    
    @DeleteMapping("{supplier_id}")
    public ResponseEntity<BaseResponseModel> deleteSupplier(@PathVariable("supplier_id") Long supplierId) {
        return supplierService.deleteSupplier(supplierId);
    }
}
