package com.app.invoice.tenant.repos;


import com.app.invoice.tenant.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    @Query(value = "SELECT invoice_number FROM invoice ORDER BY id DESC LIMIT 1", nativeQuery = true)
    String findLastInvoiceNumber();

    List<Invoice> findAllByDeletedFalse();

    Optional<Invoice> findByIdAndDeletedFalse(Long id);

    long countByDueDateBeforeAndStatusNot(LocalDate date, String status);
    long countByDueDateBeforeAndStatus(LocalDate date, String status);
    long countByDueDateAfter(LocalDate date);
    List<Invoice> findTop5ByOrderByIssueDateDesc();
}
