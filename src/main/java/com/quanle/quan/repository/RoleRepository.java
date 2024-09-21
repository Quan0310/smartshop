package com.quanle.quan.repository;

import com.quanle.quan.models.enums.ERole;
import com.quanle.quan.models.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByName(ERole name);
}
