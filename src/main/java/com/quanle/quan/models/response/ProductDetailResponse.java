package com.quanle.quan.models.response;

import com.quanle.quan.models.entity.ProductOptionSet;
import lombok.Data;

import java.util.List;

@Data
public class ProductDetailResponse {
    private String id;
    private String name;
    private String description;
    private String imageUrl;
    private List<OptionTypeResponse> optionTypeResponses;

}
