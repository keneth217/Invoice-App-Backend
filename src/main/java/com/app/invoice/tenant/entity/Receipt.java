package com.app.invoice.tenant.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class Receipt {
    @Id
    @GeneratedValue
    private Long id;

    private String receiptNumber;
    private LocalDate issueDate;
    private BigDecimal amountReceived;
    private BigDecimal balanceRemaining;
    private String paymentMethod;
    private String status; // e.g., "Paid", "Pending", "Cancelled"
    private String createdBy;
    private String description;
    private String referenceNumber;
    private boolean deleted = false;
    @ManyToOne
    private Customer customer;

    @ManyToOne
    private Invoice invoice;

    @OneToOne
    private PaymentVoucher paymentVoucher;
}
