package com.quanle.quan.models.entity;

import com.quanle.quan.models.enums.ERole;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "roles")
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ERole name;
}
