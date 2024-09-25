package com.quanle.quan.controller;

import com.quanle.quan.models.entity.DetailSpecification;
import com.quanle.quan.models.entity.ProductType;
import com.quanle.quan.models.entity.Specification;
import com.quanle.quan.models.request.DetailSpecificationRequest;
import com.quanle.quan.models.request.SpecificationRequest;
import com.quanle.quan.repository.DetailSpecificationRepository;
import com.quanle.quan.repository.SpecificationRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/detailSpecification")
public class DetailSpecificationController {
    @Autowired
    DetailSpecificationRepository detailSpecificationRepository;

    @Autowired
    SpecificationRepository specificationRepository;
    @PostMapping()
    public ResponseEntity<?> addDetailSpecification (@Valid @RequestBody DetailSpecificationRequest detailSpecificationRequest) {
        Specification specification = specificationRepository.findById(detailSpecificationRequest.getSpecificationId()).get();

        DetailSpecification detailSpecification = new DetailSpecification();
        detailSpecification.setId(UUID.randomUUID().toString());
        detailSpecification.setDetailSpecificationName(detailSpecificationRequest.getDetailSpecificationName());
        detailSpecification.setSpecification(specification);

        return ResponseEntity.ok(detailSpecificationRepository.save(detailSpecification));
    }
}
