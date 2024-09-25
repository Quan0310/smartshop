package com.quanle.quan.models.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
public class ProductRequest {
    private String name;
    private String description;
    private String imageUrl;
    private String categoryId;
    private Set<String> subCategoryIds;
}
