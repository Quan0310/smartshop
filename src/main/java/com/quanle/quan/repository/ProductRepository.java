package com.quanle.quan.repository;

import com.quanle.quan.models.entity.Category;
import com.quanle.quan.models.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

public interface ProductRepository extends JpaRepository<Product, String> {
    List<Product> findByCategory(Category category);
}