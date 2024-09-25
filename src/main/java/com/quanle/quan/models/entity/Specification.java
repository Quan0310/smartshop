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
@Table(name = "specification")
public class Specification {
    @Id
    private String id;

    private String name;

    @OneToMany(mappedBy = "specification", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DetailSpecification> detailSpecifications = new ArrayList<>();

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "product_type_id", nullable = false)
    private ProductType productType;
}
