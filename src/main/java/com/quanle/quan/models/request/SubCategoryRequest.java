package com.quanle.quan.models.request;

import com.quanle.quan.models.entity.SubCategoryType;
import lombok.Data;

@Data
public class SubCategoryRequest {
    private String name;
    private String categoryId;
    private SubCategoryType type;
}
