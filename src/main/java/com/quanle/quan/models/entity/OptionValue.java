package com.quanle.quan.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "option_value")
public class OptionValue {
    @Id
    private String id;
    private String value;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "option_type_id")
    private OptionType optionType;
}
