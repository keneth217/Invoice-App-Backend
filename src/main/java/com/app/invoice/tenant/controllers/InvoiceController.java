package com.app.invoice.tenant.controller;

import com.app.invoice.tenant.dto.InvoiceRequest;
import com.app.invoice.tenant.dto.InvoiceResponse;
import com.app.invoice.tenant.service.InvoiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/v1/invoices")


public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PostMapping
    public ResponseEntity<InvoiceResponse> createInvoice(@RequestBody InvoiceRequest invoice) {
        InvoiceResponse createdInvoice = invoiceService.createInvoice(invoice);
        return ResponseEntity.status(201).body(createdInvoice); // 201 Created


    }

    @PostMapping("/all")
    public ResponseEntity<List<InvoiceResponse>> getAllInvoices() {
        List<InvoiceResponse> invoices = invoiceService.getAllInvoices();
        return ResponseEntity.ok(invoices); // 200 OK
    }
    @PostMapping("/{id}/update")
    public ResponseEntity<InvoiceResponse> updateInvoice(  @PathVariable Long id,@RequestBody InvoiceRequest invoice) {
        InvoiceResponse updatedInvoice = invoiceService.updateInvoice(id,invoice);
        return ResponseEntity.ok(updatedInvoice); // 200 OK
    }

}
