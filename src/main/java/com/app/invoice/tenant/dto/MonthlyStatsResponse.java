package com.app.invoice.tenant.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@NoArgsConstructor
public class MonthlyStatsResponse {
    private String month;
    private BigDecimal totalInvoice;
    private BigDecimal totalExpense;
}
