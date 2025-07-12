package com.app.invoice.tenant.repos.auth;


import com.app.invoice.tenant.entity.auth.ERole;
import com.app.invoice.tenant.entity.auth.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
