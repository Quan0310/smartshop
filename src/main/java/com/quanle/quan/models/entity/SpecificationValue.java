package com.quanle.quan.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name= "specification_value")
public class SpecificationValue {
    @Id
    private String id;
    private String value;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "detail_specification_id", nullable = false)
    private DetailSpecification detailSpecification;
}
