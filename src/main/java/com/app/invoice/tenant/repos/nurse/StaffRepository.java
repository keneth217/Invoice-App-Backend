package com.app.invoice.tenant.repos.nurse;

import com.app.invoice.tenant.entity.nurse.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
}
