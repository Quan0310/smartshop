package com.quanle.quan.controller;

import com.quanle.quan.models.entity.OptionType;
import com.quanle.quan.models.entity.OptionValue;
import com.quanle.quan.models.entity.Product;
import com.quanle.quan.models.entity.ProductOptionSet;
import com.quanle.quan.models.request.OptionTypeRequest;
import com.quanle.quan.models.request.OptionValueRequest;
import com.quanle.quan.models.request.ProductOptionSetRequest;
import com.quanle.quan.repository.OptionTypeRepository;
import com.quanle.quan.repository.OptionValueRepository;
import com.quanle.quan.repository.ProductOptionSetRepository;
import com.quanle.quan.repository.ProductRepository;
import com.quanle.quan.service.ProductOptionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/options")
public class OptionController {
    @Autowired
    OptionTypeRepository optionTypeRepository;

    @Autowired
    OptionValueRepository optionValueRepository;

    @Autowired
    ProductOptionService productOptionService;

    @Autowired
    ProductOptionSetRepository productOptionSetRepository;

    @Autowired
    ProductRepository productRepository;
    @PostMapping("addOptionType")
    public ResponseEntity<?> addOptionTypeAndValue(@Valid @RequestBody OptionTypeRequest optionTypeRequest){
        OptionType optionType = new OptionType();
        optionType.setName(optionTypeRequest.getName());
        optionType.setId(UUID.randomUUID().toString());

        return ResponseEntity.ok( optionTypeRepository.save(optionType));
    }
    @PostMapping("addValue/{id}")
    public ResponseEntity<?> addValue(@Valid @RequestBody OptionValueRequest optionValueRequest, @PathVariable String id){
        OptionType optionType = productOptionService.addOptionValue(optionValueRequest, id);
        return ResponseEntity.ok(optionType);
    }
    @GetMapping("getOptionType/{id}")
    public ResponseEntity<?> getOptionType(@Valid  @PathVariable String id){

        OptionType optionType = optionTypeRepository.findById(id).get();
        return ResponseEntity.ok(optionType);
    }

    @PostMapping("addOptionSet")
    public ResponseEntity<?> addOptionSet(@Valid @RequestBody ProductOptionSetRequest productOptionSetRequest){
        Product product = productRepository.findById(productOptionSetRequest.getProductId()).get();
        Set<OptionValue> optionValues = new HashSet<OptionValue>(optionValueRepository.findAllById(productOptionSetRequest.getOptionValueIds()));

        Map<OptionType, List<OptionValue>> optionTypeToValuesMap = optionValues.stream()
                .collect(Collectors.groupingBy(OptionValue::getOptionType));

        boolean hasMultipleValuesForSameType = optionTypeToValuesMap.values().stream()
                .anyMatch(values -> values.size() > 1);

        if (hasMultipleValuesForSameType) {
            return ResponseEntity.badRequest().body("There cannot be more than one OptionValue for the same OptionType.");
        }

        for (OptionType fixedOptionType : product.getFixedOptionTypes()) {
            if (!optionTypeToValuesMap.containsKey(fixedOptionType)) {
                return ResponseEntity.badRequest().body("Missing OptionValue for OptionType: " + fixedOptionType.getName());
            }
        }

        ProductOptionSet productOptionSet = new ProductOptionSet();
        productOptionSet.setId(UUID.randomUUID().toString());
        productOptionSet.setProduct(product);
        productOptionSet.setOptionValues(optionValues);

        return ResponseEntity.ok(productOptionSetRepository.save(productOptionSet));
    }

}
