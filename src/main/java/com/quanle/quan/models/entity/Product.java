package com.quanle.quan.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "product")
public class Product {

    @Id
    private String id;

    private String name;
    private String description;
    private String imageUrl;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductOptionSet> productOptionSets = new ArrayList<>();

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "product_option_type",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "option_type_id")
    )
    private Set<OptionType> fixedOptionTypes = new HashSet<>();


    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "product_subcategory",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "subcategory_id")
    )
    private Set<SubCategory> subCategories = new HashSet<>();


    // Constructors, Getters, and Setters
    public Product() {}

    public Product(String name, String description, String imageUrl, Category category) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public void addSubCategory(SubCategory subCategory) {
        if (subCategory.getCategory().equals(this.category)) {
            this.subCategories.add(subCategory);
            subCategory.getProducts().add(this);
        } else {
            throw new IllegalArgumentException("SubCategory does not belong to the same Category as the Product");
        }
    }

}