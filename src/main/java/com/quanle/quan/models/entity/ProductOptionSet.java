package com.quanle.quan.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "product_option_set")
public class ProductOptionSet {
    @Id
    private String id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "option_set_value", joinColumns = @JoinColumn(name = "option_set_id"), inverseJoinColumns = @JoinColumn(name = "option_value_id"))
    private Set<OptionValue> optionValues = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "option_set_specification", joinColumns = @JoinColumn(name = "option_set_id"), inverseJoinColumns = @JoinColumn(name = "specification_id"))
    private Set<Specification> specifications = new HashSet<>();
}