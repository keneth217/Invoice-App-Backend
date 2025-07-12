package com.app.invoice.tenant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class LedgerReport {
    private List<LedgerTransactionResponse> transactions;
    private LedgerSummary summary;
}

