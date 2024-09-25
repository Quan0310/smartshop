package com.quanle.quan.controller;

import com.quanle.quan.models.entity.Category;
import com.quanle.quan.models.entity.SubCategory;
import com.quanle.quan.models.request.SubCategoryRequest;
import com.quanle.quan.repository.CategoryRepository;
import com.quanle.quan.repository.SubCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/subcategories")
public class SubCategoryController {

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    public List<SubCategory> getAllSubCategories() {
        return subCategoryRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubCategory> getSubCategoryById(@PathVariable String id) {
        return subCategoryRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<SubCategory> createSubCategory(@RequestBody SubCategoryRequest subCategoryRequest) {
        // Tìm Category cha dựa trên id được cung cấp
        Category parentCategory = categoryRepository.findById(subCategoryRequest.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // Tạo mới một SubCategory với tên, loại và Category cha
        SubCategory subCategory = new SubCategory();
        subCategory.setName(subCategoryRequest.getName());
        subCategory.setType(subCategoryRequest.getType());  // Set loại subcategory
        subCategory.setCategory(parentCategory);  // Set Category cha
        subCategory.setId(UUID.randomUUID().toString());  // Tạo ID ngẫu nhiên

        // Lưu subcategory vào database
        SubCategory savedSubCategory = subCategoryRepository.save(subCategory);
        return ResponseEntity.ok(savedSubCategory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubCategory> updateSubCategory(@PathVariable String id, @RequestBody SubCategory updatedSubCategory) {
        return subCategoryRepository.findById(id)
                .map(subCategory -> {
                    subCategory.setName(updatedSubCategory.getName());
                    return ResponseEntity.ok(subCategoryRepository.save(subCategory));
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSubCategory(@PathVariable String id) {
        return subCategoryRepository.findById(id)
                .map(subCategory -> {
                    subCategoryRepository.delete(subCategory);
                    return ResponseEntity.ok().build();
                }).orElse(ResponseEntity.notFound().build());
    }
}