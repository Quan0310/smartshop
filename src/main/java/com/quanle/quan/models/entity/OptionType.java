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
@Table(name = "option_type")
public class OptionType {
    @Id
    private String id;
    private String name; // Ví dụ: "Màn hình", "RAM", "Bộ nhớ"

    @JsonIgnore
    @OneToMany(mappedBy = "optionType", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OptionValue> optionValues = new ArrayList<>();
}