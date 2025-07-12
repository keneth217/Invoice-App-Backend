package com.app.invoice.tenant.controllers;

import com.app.invoice.tenant.dto.LedgerReport;
import com.app.invoice.tenant.dto.LedgerSummary;
import com.app.invoice.tenant.dto.LedgerTransactionResponse;
import com.app.invoice.tenant.services.LedgerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ledger")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Ledger Management", description = "APIs for ledger operations and financial reporting")
public class LedgerController {
    private final LedgerService ledgerService;

    public LedgerController(LedgerService ledgerService) {
        this.ledgerService = ledgerService;
    }

    @Operation(
        summary = "Get ledger summary",
        description = "Retrieves a summary of ledger entries including total credits, debits, and balance"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ledger summary retrieved successfully",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = LedgerSummary.class),
                examples = @ExampleObject(
                    name = "Success Response",
                    value = """
                    {
                        "totalCredits": 67500.00,
                        "totalDebits": 75000.00,
                        "balance": -7500.00
                    }
                    """
                )
            )
        )
    })
    @PostMapping
    public ResponseEntity<LedgerSummary> getLedgerSummary() {
        LedgerSummary summary = ledgerService.getLedgerSummary();
        return ResponseEntity.ok(summary);
    }
    @Operation(
        summary = "Get ledger transactions",
        description = "Retrieves all ledger transactions with running balance calculations"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ledger transactions retrieved successfully",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = LedgerTransactionResponse.class),
                examples = @ExampleObject(
                    name = "Success Response",
                    value = """
                    [
                        {
                            "transactionDate": "2024-01-15T10:30:00",
                            "transactionType": "INVOICE",
                            "referenceNumber": "INV-0001",
                            "debitAmount": 1500.00,
                            "creditAmount": 0.00,
                            "description": "Invoice created for customer John Doe",
                            "runningBalance": -1500.00
                        },
                        {
                            "transactionDate": "2024-01-15T14:20:00",
                            "transactionType": "PAYMENT",
                            "referenceNumber": "VCH-0001",
                            "debitAmount": 0.00,
                            "creditAmount": 1500.00,
                            "description": "Payment received for invoice INV-0001",
                            "runningBalance": 0.00
                        }
                    ]
                    """
                )
            )
        )
    })
    @PostMapping("/transactions")
    public ResponseEntity<List<LedgerTransactionResponse>> getLedgerTransactions() {
        return ResponseEntity.ok(ledgerService.getLedgerTransactions());
    }
    @PostMapping ("/summary")
    public ResponseEntity<LedgerReport> getLedgerSummaryDetails() {
       return  ResponseEntity.ok(ledgerService.getLedgerWithSummary());
    }
}
