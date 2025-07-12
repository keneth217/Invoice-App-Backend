package com.app.invoice.tenant.mapper;


import com.app.invoice.tenant.dto.LedgerTransactionResponse;
import com.app.invoice.tenant.entity.LedgerEntry;

import java.math.BigDecimal;

public class LedgerMapper {

    public  static LedgerEntry toLedgerEntry(LedgerTransactionResponse ledgerEntry) {

     return LedgerEntry.builder()
             .paymentMethod(ledgerEntry.getPaymentMethod())
                .referenceNumber(ledgerEntry.getReferenceNumber())
                .transactionDate(ledgerEntry.getTransactionDate())
                .transactionType(ledgerEntry.getTransactionType())
                .description(ledgerEntry.getDescription())
                .creditAmount(ledgerEntry.getCreditAmount())
                .debitAmount(ledgerEntry.getDebitAmount())
             .description(ledgerEntry.getDescription())

                .build();
    }

    public static LedgerTransactionResponse toTransactionResponse(LedgerEntry ledgerEntry) {
        return LedgerTransactionResponse.builder()
                .transactionDate(ledgerEntry.getTransactionDate())
                .transactionType(ledgerEntry.getTransactionType())
                .referenceNumber(ledgerEntry.getReferenceNumber())
                .debitAmount(ledgerEntry.getDebitAmount() != null ? ledgerEntry.getDebitAmount() : BigDecimal.ZERO)
                .creditAmount(ledgerEntry.getCreditAmount() != null ? ledgerEntry.getCreditAmount() : BigDecimal.ZERO)
                .description(ledgerEntry.getDescription())
                .description(ledgerEntry.getDescription())
                .paymentMethod(ledgerEntry.getPaymentMethod())
                .build();
    }
}
