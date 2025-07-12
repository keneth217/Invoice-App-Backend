package com.app.invoice.tenant.entity;

import com.app.invoice.tenant.enums.EntryType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class LedgerEntry {
    @Id
    @GeneratedValue
    private Long id;

    private LocalDateTime transactionDate;
    private String transactionType; // INVOICE, PAYMENT, RECEIPT, etc.
    private String referenceNumber;
    private BigDecimal debitAmount;
    private BigDecimal creditAmount;
    private String description;
    private String customerName;
    private String paymentMethod;
    private String createdBy;
    private UUID customerId;
    private boolean deleted = false;
    private LocalDate entryDate;

    @Enumerated(EnumType.STRING)
    private EntryType type; // CREDIT or DEBIT

    private Long invoiceId;
    private Long receiptId;
    private Long paymentVoucherId;

    private LocalDateTime createdAt;
}
