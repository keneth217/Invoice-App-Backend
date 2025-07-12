package com.app.invoice.tenant.repos;


import com.app.invoice.tenant.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findAllByDeletedFalse();
    int countByDeletedFalse();
}
