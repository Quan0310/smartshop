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
@Table(name = "detail_specification")
public class DetailSpecification {
    @Id
    private String id;
    private String detailSpecificationName;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "specification_id", nullable = false)
    private Specification specification;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "detailSpecification")
    private List<SpecificationValue> specificationValues =new ArrayList<>();
}
