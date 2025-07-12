package com.app.invoice.tenant.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.time.LocalDate;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class PaymentVoucher {
    @Id
    @GeneratedValue
    private Long id;
    private String voucherNumber;
    private LocalDate paymentDate;
    private BigDecimal amountPaid;
    @Enumerated(EnumType.STRING)
    private com.app.invoice.enums.PaymentMethod paymentMethod;
    private String referenceNumber;
    @ManyToOne
    @JsonBackReference
    private Invoice invoice;
    private  boolean deleted = false;

    private LocalDate createdAt = LocalDate.now();
    private LocalDate updatedAt = LocalDate.now();
    private String createdBy;
    private String updatedBy;
}
