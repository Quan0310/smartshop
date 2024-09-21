package com.quanle.quan.models.entity;

import com.quanle.quan.models.enums.AuthProvider;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "user_providers")
public class UserProvider {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuthProvider provider;

    private String providerId;
}