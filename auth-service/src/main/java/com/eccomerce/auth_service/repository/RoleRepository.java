package com.eccomerce.auth_service.repository;

import com.eccomerce.auth_service.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findRoleByRoleName(String name);
}
