package com.quanle.quan.models.response;

import com.quanle.quan.models.entity.OptionValue;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class OptionTypeResponse {
    private String id;
    private String name;
    private List<OptionValueResponse> optionValues;
}
