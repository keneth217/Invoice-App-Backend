package com.app.invoice.master.repos;


import com.app.invoice.master.entity.MasterERole;
import com.app.invoice.master.entity.MasterRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MasterRoleRepository extends JpaRepository<MasterRole, Long> {
    Optional<MasterRole> findByName(MasterERole name);
}
