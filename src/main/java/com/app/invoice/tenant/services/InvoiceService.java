package com.app.invoice.tenant.services;



import com.app.invoice.tenant.dto.InvoiceRequest;
import com.app.invoice.tenant.dto.InvoiceResponse;

import java.util.List;


public interface InvoiceService {
    InvoiceResponse createInvoice(InvoiceRequest invoice);

    List<InvoiceResponse> getAllInvoices();

    InvoiceResponse getInvoiceById(Long id);

    InvoiceResponse updateInvoice(Long id, InvoiceRequest invoiceRequest);

    InvoiceResponse deleteInvoice(Long id);

    List<InvoiceResponse> getInvoicesByCustomerId(Long customerId);

    List<InvoiceResponse> getInvoicesByStatus(String status);

}
