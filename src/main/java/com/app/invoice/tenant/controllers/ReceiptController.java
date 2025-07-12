package com.app.invoice.tenant.controller;


import com.app.invoice.tenant.dto.ReceiptResponse;
import com.app.invoice.tenant.service.LedgerService;
import com.app.invoice.tenant.service.ReceiptService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/receipts")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ReceiptController {


    private final ReceiptService receiptService;

    public ReceiptController(ReceiptService receiptService) {
        this.receiptService = receiptService;
    }


    @PostMapping("all")
    public ResponseEntity<List<ReceiptResponse>> getAllReceipts() {
        List<ReceiptResponse> receipts = receiptService.getAllReceipts();
        return ResponseEntity.ok(receipts);
    }
    @PostMapping("by-voucher")
    public ResponseEntity<List<ReceiptResponse>> getReceiptsByPaymentVoucherId(@RequestBody Long paymentVoucherId) {
        List<ReceiptResponse> receipts = receiptService.getReceiptsByPaymentVoucherId(paymentVoucherId);
        return ResponseEntity.ok(receipts);
    }
}
