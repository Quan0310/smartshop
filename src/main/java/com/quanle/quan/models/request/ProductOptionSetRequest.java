package com.quanle.quan.models.request;

import lombok.Data;

import java.util.Set;

@Data
public class ProductOptionSetRequest {
    private String productId;
    private Set<String> optionValueIds;

}
