package com.quanle.quan.controller;

import com.quanle.quan.models.entity.*;
import com.quanle.quan.models.request.FixedOptionTypeRequest;
import com.quanle.quan.models.request.ProductRequest;
import com.quanle.quan.models.response.OptionTypeResponse;
import com.quanle.quan.models.response.OptionValueResponse;
import com.quanle.quan.models.response.ProductDetailResponse;
import com.quanle.quan.repository.CategoryRepository;
import com.quanle.quan.repository.OptionTypeRepository;
import com.quanle.quan.repository.ProductRepository;
import com.quanle.quan.repository.SubCategoryRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private OptionTypeRepository optionTypeRepository;
    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable String id) {
        Product product = productRepository.findById(id).get();

//        Map<OptionType, List<OptionValueResponse>> optionTypeToValuesMap = product.getProductOptionSets().stream().
        Map<OptionType, List<OptionValueResponse>> optionTypeToValuesMap = product.getProductOptionSets().stream()
                .flatMap(productOptionSet -> productOptionSet.getOptionValues().stream())
                    .collect(Collectors.groupingBy(OptionValue::getOptionType,
                        Collectors.mapping(optionValue ->
                                new OptionValueResponse(optionValue.getId(), optionValue.getValue()), Collectors.toList())));

        List<OptionTypeResponse> optionTypeResponses = optionTypeToValuesMap.entrySet().stream()
                .map(entry -> {
                    OptionType optionType = entry.getKey();
                    List<OptionValueResponse> optionValues = entry.getValue();
                    return new OptionTypeResponse(optionType.getId(), optionType.getName(), optionValues);
                }).collect(Collectors.toList());


        ProductDetailResponse response = new ProductDetailResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setImageUrl(product.getImageUrl());
        response.setOptionTypeResponses(optionTypeResponses);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody ProductRequest productRequest) {
        // Tìm Category theo ID
        Category category = categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // Tìm tất cả các SubCategory theo ID
        Set<SubCategory> subCategories = new HashSet<>();
        for (String subCategoryId : productRequest.getSubCategoryIds()) {
            SubCategory subCategory = subCategoryRepository.findById(subCategoryId)
                    .orElseThrow(() -> new RuntimeException("SubCategory not found"));
            subCategories.add(subCategory);
        }

        // Tạo mới Product
        Product product = new Product();
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setImageUrl(productRequest.getImageUrl());
        product.setId(UUID.randomUUID().toString());  // Tạo ID ngẫu nhiên

        // Liên kết Category và SubCategory với Product
        product.setCategory(category);
        product.setSubCategories(subCategories);

        // Lưu Product vào database
        Product savedProduct = productRepository.save(product);
        return ResponseEntity.ok(savedProduct);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable String id, @RequestBody Product updatedProduct) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setName(updatedProduct.getName());
                    product.setDescription(updatedProduct.getDescription());
                    product.setImageUrl(updatedProduct.getImageUrl());
                    return ResponseEntity.ok(productRepository.save(product));
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable String id) {
        return productRepository.findById(id)
                .map(product -> {
                    productRepository.delete(product);
                    return ResponseEntity.ok().build();
                }).orElse(ResponseEntity.notFound().build());
    }
    @PostMapping("addFixedOptionType")
    public ResponseEntity<?> addFixedOptionType(@Valid @RequestBody FixedOptionTypeRequest fixedOptionTypeRequest) {
       Product product = productRepository.findById(fixedOptionTypeRequest.getProductId()).get();
       OptionType optionType = optionTypeRepository.findById(fixedOptionTypeRequest.getOptionTypeId()).get();
       product.getFixedOptionTypes().add(optionType);

       return ResponseEntity.ok(productRepository.save(product));
    }
}