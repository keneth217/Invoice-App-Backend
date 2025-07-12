package com.app.invoice.tenant.repos;


import com.app.invoice.tenant.entity.PaymentVoucher;
import com.app.invoice.tenant.entity.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Arrays;
import java.util.List;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {

    @Query(value = "SELECT receipt_number FROM receipt ORDER BY id DESC LIMIT 1", nativeQuery = true)
    String findLastVoucherNumber();

    List<Receipt> findAllByDeletedFalse();
    List<Receipt> findAllByPaymentVoucherAndDeletedFalse(PaymentVoucher paymentVoucher);


    long countByDeletedFalse();
    List<Receipt> findTop5ByOrderByIssueDateDesc();
}
