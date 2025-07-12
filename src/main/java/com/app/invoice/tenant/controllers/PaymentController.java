package com.app.invoice.tenant.controller;


import com.app.invoice.tenant.dto.PaymentVoucherRequest;
import com.app.invoice.tenant.dto.PaymentVoucherResponse;
import com.app.invoice.tenant.service.PaymentVoucherService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PaymentController {
    private final PaymentVoucherService paymentVoucherService;

    public PaymentController(PaymentVoucherService paymentVoucherService) {
        this.paymentVoucherService = paymentVoucherService;
    }

    @PostMapping("/{invoiceId}/pay")
    public ResponseEntity<PaymentVoucherResponse> createPaymentVoucher(@PathVariable Long invoiceId, @RequestBody PaymentVoucherRequest request) {
        PaymentVoucherResponse response = paymentVoucherService.payInvoice(invoiceId,request);
        return ResponseEntity.status(HttpStatus.OK) // 200 OK
                .body(response);
    }
    @PostMapping("/update")
    public ResponseEntity<PaymentVoucherResponse> updatePaymentVoucher(@RequestBody Long id, PaymentVoucherRequest paymentVoucherRequest) {
        PaymentVoucherResponse response =paymentVoucherService.updatePaymentVoucher(id,paymentVoucherRequest) ;
        return ResponseEntity.status(HttpStatus.OK) // 200 OK
                .body(response);
    }
    @PostMapping("/delete")
    public ResponseEntity<PaymentVoucherResponse> deletePaymentVoucher(@RequestBody PaymentVoucherRequest request) {
        PaymentVoucherResponse response = paymentVoucherService.deletePaymentVoucher(request.getId());
        return ResponseEntity.status(HttpStatus.OK) // 200 OK
                .body(response);
    }
    @PostMapping("/{id}")
    public ResponseEntity<PaymentVoucherResponse> getPaymentVoucherById(@RequestBody PaymentVoucherRequest request) {
        PaymentVoucherResponse response =paymentVoucherService.getPaymentVoucherById(request.getId());
        return ResponseEntity.status(HttpStatus.OK) // 200 OK
                .body(response);
    }
    @PostMapping("/all")
    public ResponseEntity<List<PaymentVoucherResponse>> getAllPaymentVouchers() {
        List<PaymentVoucherResponse> response = paymentVoucherService.getAllPaymentVouchers();
        return ResponseEntity.status(HttpStatus.OK) // 200 OK
                .body(response);
    }
    @PostMapping("/invoice/{invoiceId}/vouchers")
    public ResponseEntity<List<PaymentVoucherResponse>> getPaymentVouchersByInvoiceId(@PathVariable Long invoiceId) {
        List<PaymentVoucherResponse> response = paymentVoucherService.getPaymentVouchersByInvoiceId(invoiceId);
        return ResponseEntity.status(HttpStatus.OK) // 200 OK
                .body(response);
    }


}
