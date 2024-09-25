package com.quanle.quan.models.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "category")
public class Category {

    @Id
    private String id;

    private String name;

    @OneToMany(mappedBy = "category",fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products = new ArrayList<>();

    // Constructors, Getters, and Setters
    public Category() {}

    public Category(String name) {
        this.name = name;
    }
}