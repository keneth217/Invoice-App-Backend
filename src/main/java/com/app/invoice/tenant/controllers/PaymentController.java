package com.app.invoice.tenant.controllers;

import com.app.invoice.tenant.dto.PaymentVoucherRequest;
import com.app.invoice.tenant.dto.PaymentVoucherResponse;
import com.app.invoice.tenant.services.PaymentVoucherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Payment Management", description = "APIs for payment processing and voucher management")
public class PaymentController {
    private final PaymentVoucherService paymentVoucherService;

    public PaymentController(PaymentVoucherService paymentVoucherService) {
        this.paymentVoucherService = paymentVoucherService;
    }

    @Operation(
        summary = "Process payment for an invoice",
        description = "Creates a payment voucher and processes payment for a specific invoice"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Payment processed successfully",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = PaymentVoucherResponse.class),
                examples = @ExampleObject(
                    name = "Success Response",
                    value = """
                    {
                        "id": 1,
                        "voucherNumber": "VCH-0001",
                        "paymentDate": "2024-01-15",
                        "amountPaid": 1500.00,
                        "paymentMethod": "CASH",
                        "referenceNumber": "REF-123456",
                        "description": "Payment for invoice INV-0001",
                        "invoiceId": 1
                    }
                    """
                )
            )
        ),
        @ApiResponse(responseCode = "404", description = "Invoice not found"),
        @ApiResponse(responseCode = "400", description = "Invalid payment data or amount exceeds invoice total")
    })
    @PostMapping("/{invoiceId}/pay")
    public ResponseEntity<PaymentVoucherResponse> createPaymentVoucher(
        @Parameter(description = "Invoice ID", required = true)
        @PathVariable Long invoiceId,
        @Parameter(description = "Payment details", required = true)
        @RequestBody PaymentVoucherRequest request) {
        PaymentVoucherResponse response = paymentVoucherService.payInvoice(invoiceId, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
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
    @Operation(
        summary = "Get all payment vouchers",
        description = "Retrieves all payment vouchers for the current tenant"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Payment vouchers retrieved successfully",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = PaymentVoucherResponse.class),
                examples = @ExampleObject(
                    name = "Success Response",
                    value = """
                    [
                        {
                            "id": 1,
                            "voucherNumber": "VCH-0001",
                            "paymentDate": "2024-01-15",
                            "amountPaid": 1500.00,
                            "paymentMethod": "CASH",
                            "referenceNumber": "REF-123456",
                            "description": "Payment for invoice INV-0001",
                            "invoiceId": 1
                        },
                        {
                            "id": 2,
                            "voucherNumber": "VCH-0002",
                            "paymentDate": "2024-01-14",
                            "amountPaid": 2500.00,
                            "paymentMethod": "BANK_TRANSFER",
                            "referenceNumber": "REF-789012",
                            "description": "Payment for invoice INV-0002",
                            "invoiceId": 2
                        }
                    ]
                    """
                )
            )
        )
    })
    @PostMapping("/all")
    public ResponseEntity<List<PaymentVoucherResponse>> getAllPaymentVouchers() {
        List<PaymentVoucherResponse> response = paymentVoucherService.getAllPaymentVouchers();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @PostMapping("/invoice/{invoiceId}/vouchers")
    public ResponseEntity<List<PaymentVoucherResponse>> getPaymentVouchersByInvoiceId(@PathVariable Long invoiceId) {
        List<PaymentVoucherResponse> response = paymentVoucherService.getPaymentVouchersByInvoiceId(invoiceId);
        return ResponseEntity.status(HttpStatus.OK) // 200 OK
                .body(response);
    }


}
