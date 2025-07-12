package com.app.invoice.tenant.service;


import com.app.invoice.tenant.dto.LedgerReport;
import com.app.invoice.tenant.dto.LedgerSummary;
import com.app.invoice.tenant.dto.LedgerTransactionResponse;
import com.app.invoice.tenant.dto.ReceiptResponse;
import com.app.invoice.tenant.entity.Invoice;
import com.app.invoice.tenant.entity.PaymentVoucher;


import java.util.List;


public interface LedgerService {


    void updateLedgerWithInvoice(Invoice invoice);

    void updateLedgerPayment(PaymentVoucher savedVoucher);

    LedgerSummary getLedgerSummary();

    List<LedgerTransactionResponse> getLedgerTransactions();

    LedgerReport getLedgerWithSummary();

}
