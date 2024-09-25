package com.quanle.quan.models.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "product_tyoe")
public class ProductType {
    @Id
    private String id;
    private String name;

    @OneToMany(mappedBy = "productType", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Specification> specifications = new ArrayList<>();
}
