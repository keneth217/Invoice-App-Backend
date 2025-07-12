package com.app.invoice.tenant.dto;


import com.app.invoice.tenant.entity.Customer;
import com.app.invoice.tenant.entity.InvoiceItem;
import com.app.invoice.tenant.entity.PaymentVoucher;
import com.app.invoice.tenant.entity.Quotation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class InvoiceResponse {

    private Long id;
    private String invoiceNumber;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private BigDecimal totalAmount;
    private String status;
    private Customer customer;
    private List<InvoiceItem> invoiceItems;
    private List<PaymentVoucher> paymentVouchers;
    private Quotation quotation;

}
