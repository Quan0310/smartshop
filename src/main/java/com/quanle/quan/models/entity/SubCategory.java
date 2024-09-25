package com.quanle.quan.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "subcategory")
public class SubCategory {

    @Id
    private String id;

    private String name;

    @Enumerated(EnumType.STRING)  // Enum cho loại subcategory
    private SubCategoryType type;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToMany(mappedBy = "subCategories")
    private Set<Product> products = new HashSet<>();
}