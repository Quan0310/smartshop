package com.quanle.quan.controller;

import com.quanle.quan.models.entity.ProductType;
import com.quanle.quan.models.entity.Specification;
import com.quanle.quan.models.request.ProductTypeRequest;
import com.quanle.quan.models.request.SpecificationRequest;
import com.quanle.quan.repository.ProductTypeRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/productType")
public class ProductTypeController {
    @Autowired
    ProductTypeRepository productTypeRepository;

    @PostMapping
    public ResponseEntity<?> addProductType (@Valid @RequestBody ProductTypeRequest productTypeRequest) {
        ProductType productType = new ProductType();
        productType.setName(productTypeRequest.getName());
        productType.setId(UUID.randomUUID().toString());

        return ResponseEntity.ok(productTypeRepository.save(productType));
    }
    @GetMapping
    public ResponseEntity<?> getAllSpecifications () {
        return ResponseEntity.ok(productTypeRepository.findAll());
    }
}
