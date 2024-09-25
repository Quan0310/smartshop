package com.quanle.quan.controller;

import com.quanle.quan.models.entity.Category;
import com.quanle.quan.models.entity.Product;
import com.quanle.quan.models.request.CategoryRequest;
import com.quanle.quan.repository.CategoryRepository;
import com.quanle.quan.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryService categoryService;
    @GetMapping
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable String id) {
        Category category = categoryService.getCategoryById(id);
        System.out.println("qjcjbcd : "+ category.getProducts());
        return ResponseEntity.ok(category.getProducts());
    }

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody CategoryRequest categoryRequest) {
        System.out.println("aacreate");
        if (categoryRepository.findByName(categoryRequest.getName()).isPresent()) {
            return ResponseEntity.badRequest().build();
        }

        // Tạo mới một category với tên
        Category category = new Category();
        category.setName(categoryRequest.getName());
        category.setId(UUID.randomUUID().toString());  // Tạo ID ngẫu nhiên

        // Lưu category vào database
        Category savedCategory = categoryRepository.save(category);
        return ResponseEntity.ok(savedCategory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable String id, @RequestBody Category updatedCategory) {
        return categoryRepository.findById(id)
                .map(category -> {
                    category.setName(updatedCategory.getName());
                    return ResponseEntity.ok(categoryRepository.save(category));
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable String id) {
        return categoryRepository.findById(id)
                .map(category -> {
                    categoryRepository.delete(category);
                    return ResponseEntity.ok().build();
                }).orElse(ResponseEntity.notFound().build());
    }
}