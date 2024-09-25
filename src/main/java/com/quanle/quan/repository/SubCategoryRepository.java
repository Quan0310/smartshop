package com.quanle.quan.repository;

import com.quanle.quan.models.entity.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubCategoryRepository extends JpaRepository<SubCategory, String> {
    List<SubCategory> findByCategoryId(String categoryId);
}