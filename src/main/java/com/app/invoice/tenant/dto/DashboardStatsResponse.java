package com.app.invoice.tenant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class DashboardStatsResponse {
    private long totalCustomers;
    private long totalInvoices;
    private long totalReceipts;
    private long totalVouchers;
    private double totalSales;
    private BigDecimal totalPayments;
    private double totalExpenses;
    private double totalProfit;
    private List<InvoiceResponse> recentInvoices;
    private List<ReceiptResponse> recentReceipts;
    private List<PaymentVoucherResponse> recentVouchers;
    private  long expiredInvoicesCount;
    private long overdueInvoicesCount;
    private long upcomingInvoicesCount;
    private List<MonthlyStatsResponse> monthlyStats;
    private List<LedgerTransactionResponse> recentTransactions;
}
