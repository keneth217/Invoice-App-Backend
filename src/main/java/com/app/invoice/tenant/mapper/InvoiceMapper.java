package com.app.invoice.tenant.mapper;


import com.app.invoice.tenant.dto.InvoiceRequest;
import com.app.invoice.tenant.dto.InvoiceResponse;
import com.app.invoice.tenant.entity.Invoice;

public class InvoiceMapper {
    public  static Invoice toEntity(InvoiceRequest dto) {
        return Invoice.builder()
                .id(dto.getId())
                .invoiceNumber(dto.getInvoiceNumber())
                .issueDate(dto.getIssueDate())
                .dueDate(dto.getDueDate())
                .totalAmount(dto.getTotalAmount())
                .status(dto.getStatus())
                .customer(dto.getCustomer())
                .invoiceItems(dto.getInvoiceItems())
                .paymentVouchers(dto.getPaymentVouchers())
                .quotation(dto.getQuotation())

                .build();
    }

    public static InvoiceResponse toResponse(Invoice invoice) {
        return InvoiceResponse.builder()
                .id(invoice.getId())
                .invoiceNumber(invoice.getInvoiceNumber())
                .issueDate(invoice.getIssueDate())
                .dueDate(invoice.getDueDate())
                .totalAmount(invoice.getTotalAmount())
                .status(invoice.getStatus())
                .customer(invoice.getCustomer())
                .invoiceItems(invoice.getInvoiceItems())
                .paymentVouchers(invoice.getPaymentVouchers())
                .quotation(invoice.getQuotation())

                .build();
    }
}
