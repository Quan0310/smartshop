package com.quanle.quan.controller;

import com.quanle.quan.models.entity.ProductType;
import com.quanle.quan.models.entity.Specification;
import com.quanle.quan.models.request.SpecificationRequest;
import com.quanle.quan.repository.ProductTypeRepository;
import com.quanle.quan.repository.SpecificationRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/specification")
public class SpecificationController {
    @Autowired
    SpecificationRepository specificationRepository;

    @Autowired
    ProductTypeRepository productTypeRepository;
    @PostMapping()
    public ResponseEntity<?> addSpecification (@Valid @RequestBody SpecificationRequest specificationRequest) {
        ProductType productType = productTypeRepository.findById(specificationRequest.getProductTypeId()).get();


        Specification specification = new Specification();
        specification.setName(specificationRequest.getName());
        specification.setId(UUID.randomUUID().toString());
        specification.setProductType(productType);

        return ResponseEntity.ok(specificationRepository.save(specification));
    }
    @GetMapping
    public ResponseEntity<?> getAllSpecifications () {
        return ResponseEntity.ok(specificationRepository.findAll());
    }


}
