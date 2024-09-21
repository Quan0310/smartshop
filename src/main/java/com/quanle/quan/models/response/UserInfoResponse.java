package com.quanle.quan.models.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserInfoResponse {
    private String name;
    private String email;
    private String username;
    private String imageUrl;

}
