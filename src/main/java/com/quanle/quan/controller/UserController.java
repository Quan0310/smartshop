package com.quanle.quan.controller;

import com.quanle.quan.exception.ResourceNotFoundException;
import com.quanle.quan.models.entity.Product;
import com.quanle.quan.models.entity.User;
import com.quanle.quan.models.response.UserInfoResponse;
import com.quanle.quan.repository.UserRepository;
import com.quanle.quan.security.CurrentUser;
import com.quanle.quan.security.services.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/me")
//    @PreAuthorize("hasRole('ROLE_USER')")
    public UserInfoResponse getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        User user =  userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
        return new UserInfoResponse(user.getName(), user.getEmail(), user.getUsername(), user.getImageUrl());
    }

    @GetMapping("/test")
    public String test() {
        return "quanquan";
    }
}
