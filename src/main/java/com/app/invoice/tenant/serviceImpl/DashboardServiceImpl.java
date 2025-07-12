package com.app.invoice.tenant.serviceImpl;


import com.app.invoice.tenant.dto.DashboardStatsResponse;
import com.app.invoice.tenant.dto.LedgerTransactionResponse;
import com.app.invoice.tenant.dto.MonthlyStatsResponse;
import com.app.invoice.tenant.entity.Invoice;
import com.app.invoice.tenant.entity.PaymentVoucher;
import com.app.invoice.tenant.mapper.InvoiceMapper;
import com.app.invoice.tenant.mapper.LedgerMapper;
import com.app.invoice.tenant.mapper.PaymentVoucherMapper;
import com.app.invoice.tenant.mapper.ReceiptMapper;
import com.app.invoice.tenant.repos.*;
import com.app.invoice.tenant.service.DashboardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final CustomerRepository customerRepository;
    private final InvoiceRepository invoiceRepository;
    private final ReceiptRepository receiptRepository;
    private final PaymentVoucherRepository paymentVoucherRepository;

    private final LedgerRepository ledgerTransactionRepository;

    public DashboardServiceImpl(CustomerRepository customerRepository,
                                InvoiceRepository invoiceRepository,
                                ReceiptRepository receiptRepository,
                                PaymentVoucherRepository paymentVoucherRepository, LedgerRepository ledgerTransactionRepository) {
        this.customerRepository = customerRepository;
        this.invoiceRepository = invoiceRepository;
        this.receiptRepository = receiptRepository;
        this.paymentVoucherRepository = paymentVoucherRepository;
        this.ledgerTransactionRepository = ledgerTransactionRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public DashboardStatsResponse getDashboardStats() {
        int totalCustomers = (int) customerRepository.countByDeletedFalse();
        int totalInvoices = (int) invoiceRepository.count();
        int totalReceipts = (int) receiptRepository.countByDeletedFalse();
        int totalVouchers = (int) paymentVoucherRepository.count();

        List<Invoice> allInvoices = invoiceRepository.findAll().stream()
                .filter(invoice -> invoice.getIssueDate() != null)
                .collect(Collectors.toList());

        List<PaymentVoucher> allVouchers = paymentVoucherRepository.findAll().stream()
                .filter(voucher -> voucher.getPaymentDate() != null)
                .collect(Collectors.toList());

        double totalSales = allInvoices.stream()
                .map(Invoice::getTotalAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .doubleValue();
        //total of amount paid in vouchers
        BigDecimal totalPayments = paymentVoucherRepository.findAll().stream()
                .map(PaymentVoucher::getAmountPaid)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        System.out.println("Total payments: " + totalPayments);

        System.out.println("Total Sales: " + totalSales);
        System.out.println("Total Payments: " + totalPayments);
        //show total of payments  and invoices

//show recent 5 transactions from ledger
        List<LedgerTransactionResponse> recentTransaction = ledgerTransactionRepository.findTop5ByOrderByTransactionDateDesc()
                .stream().map(LedgerMapper::toTransactionResponse).toList();




        double totalExpenses = 0.0;
        double totalProfit = totalSales - totalExpenses;

        Map<YearMonth, List<Invoice>> invoicesGroupedByMonth = allInvoices.stream()
                .collect(Collectors.groupingBy(invoice -> YearMonth.from(invoice.getIssueDate())));

        List<MonthlyStatsResponse> monthlyStats = invoicesGroupedByMonth.entrySet().stream()
                .map(entry -> {
                    YearMonth yearMonth = entry.getKey();
                    BigDecimal monthlyInvoiceTotal = entry.getValue().stream()
                            .map(Invoice::getTotalAmount)
                            .filter(Objects::nonNull)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    BigDecimal monthlyPayments = allVouchers.stream()
                            .filter(voucher -> voucher.getPaymentDate() != null)
                            .filter(voucher -> yearMonth.equals(YearMonth.from(voucher.getPaymentDate())))
                            .map(PaymentVoucher::getAmountPaid)
                            .filter(Objects::nonNull)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    System.out.println("Monthly Invoice Total for " + yearMonth + ": " + monthlyInvoiceTotal);
                    System.out.println("Monthly Payments for " + yearMonth + ": " + monthlyPayments);

                    MonthlyStatsResponse response = new MonthlyStatsResponse();
                    response.setMonth(yearMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH));
                    response.setTotalInvoice(monthlyInvoiceTotal);
                    response.setTotalExpense(monthlyPayments);
                    return response;
                })
                .sorted(Comparator.comparing(MonthlyStatsResponse::getMonth))
                .toList();

        LocalDate today = LocalDate.now();

        return new DashboardStatsResponse(
                totalCustomers,
                totalInvoices,
                totalReceipts,
                totalVouchers,
                totalSales,
                totalPayments,
                totalExpenses,
                totalProfit,
                invoiceRepository.findTop5ByOrderByIssueDateDesc()
                        .stream().map(InvoiceMapper::toResponse).toList(),
                receiptRepository.findTop5ByOrderByIssueDateDesc()
                        .stream().map(ReceiptMapper::toResponse).toList(),
                paymentVoucherRepository.findTop5ByOrderByPaymentDateDesc()
                        .stream().map(PaymentVoucherMapper::toResponse).toList(),
                invoiceRepository.countByDueDateBeforeAndStatusNot(today, "PAID"),
                invoiceRepository.countByDueDateBeforeAndStatus(today.plusDays(7), "UNPAID"),
                invoiceRepository.countByDueDateAfter(today),
                monthlyStats,
                recentTransaction
        );
    }
}