package com.ecombackend.repository;

import com.ecombackend.entity.Role;
import com.ecombackend.entity.enums.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(AppRole appRole);
}