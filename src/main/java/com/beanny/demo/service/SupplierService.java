package com.beanny.demo.service;

import com.beanny.demo.dto.supplier.SupplierDto;
import com.beanny.demo.dto.supplier.UpdateSupplierDto;
import com.beanny.demo.entity.Supplier;
import com.beanny.demo.exception.model.DuplicateResourceException;
import com.beanny.demo.exception.model.ResourceNotFoundException;
import com.beanny.demo.mapper.SupplierMapper;
import com.beanny.demo.model.BaseResponseModel;
import com.beanny.demo.model.BaseResponseWithDataModel;
import com.beanny.demo.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SupplierService {
    @Autowired
    private SupplierRepository supplierRepository;
    
    @Autowired
    private SupplierMapper mapper;
    
    public ResponseEntity<BaseResponseWithDataModel> listSuppliers() {
        List<Supplier> suppliers = supplierRepository.findAll();
        
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        new BaseResponseWithDataModel(
                                "success",
                                "successfully retrieved suppliers",
                                mapper.toDtoList(suppliers)
                        )
                );
    }
    
    public ResponseEntity<BaseResponseModel> createSupplier(SupplierDto dto) {
        // if duplicate supplier name , then reject
        if(supplierRepository.existsByName(dto.getName())) {
            throw new DuplicateResourceException("supplier already existed with name: " + dto.getName());
        }
        
        Supplier supplier = mapper.toEntity(dto);
        
        supplierRepository.save(supplier);
        
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseModel("success","successfully created supplier"));
    }
    
    public ResponseEntity<BaseResponseModel> updateSupplier(Long supplierId, UpdateSupplierDto dto) {
        Supplier existingSupplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new ResourceNotFoundException("supplier not found with id: " + supplierId));
        
        mapper.updateEntityFromDto(existingSupplier,dto);
        
        supplierRepository.save(existingSupplier);
        
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseModel("success","successfully updated supplier"));
    }
    
    public ResponseEntity<BaseResponseModel> deleteSupplier(Long supplierId) {
        if(!supplierRepository.existsById(supplierId)) {
            throw new ResourceNotFoundException("supplier not found with id: " + supplierId);
        }
        
        supplierRepository.deleteById(supplierId);
        
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BaseResponseModel("success","successfully deleted supplier"));
    }
    
}
