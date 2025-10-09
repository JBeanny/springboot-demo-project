package com.beanny.demo.service;

import com.beanny.demo.common.config.ApplicationConfiguration;
import com.beanny.demo.dto.base.PaginatedResponse;
import com.beanny.demo.dto.supplier.SupplierDto;
import com.beanny.demo.dto.supplier.SupplierResponseDto;
import com.beanny.demo.dto.supplier.UpdateSupplierDto;
import com.beanny.demo.entity.Supplier;
import com.beanny.demo.exception.model.DuplicateResourceException;
import com.beanny.demo.exception.model.ResourceNotFoundException;
import com.beanny.demo.mapper.SupplierMapper;
import com.beanny.demo.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierService {
    @Autowired
    private SupplierRepository supplierRepository;
    
    @Autowired
    private SupplierMapper mapper;

    @Autowired
    private ApplicationConfiguration appConfig;

    public PaginatedResponse listSuppliersWithPagination(Pageable pageable) {
        Page<Supplier> supplierPages = supplierRepository.findAll(pageable);
        Page<SupplierResponseDto> supplierPagesDto = supplierPages.map(supplier -> mapper.toDto(supplier));

        return PaginatedResponse.from(supplierPagesDto,appConfig.getPagination().getUrlByResource("supplier"));
    }
    
    public List<SupplierResponseDto> listSuppliers() {
        List<Supplier> suppliers = supplierRepository.findAll();
        
        return mapper.toDtoList(suppliers);
    }
    
    public void createSupplier(SupplierDto dto) {
        // if duplicate supplier name , then reject
        if(supplierRepository.existsByName(dto.getName())) {
            throw new DuplicateResourceException("supplier already existed with name: " + dto.getName());
        }
        
        Supplier supplier = mapper.toEntity(dto);
        
        supplierRepository.save(supplier);
    }
    
    public void updateSupplier(Long supplierId, UpdateSupplierDto dto) {
        Supplier existingSupplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new ResourceNotFoundException("supplier not found with id: " + supplierId));
        
        mapper.updateEntityFromDto(existingSupplier,dto);
        
        supplierRepository.save(existingSupplier);
    }
    
    public void deleteSupplier(Long supplierId) {
        if(!supplierRepository.existsById(supplierId)) {
            throw new ResourceNotFoundException("supplier not found with id: " + supplierId);
        }
        
        supplierRepository.deleteById(supplierId);
    }
    
}
