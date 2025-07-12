package com.app.invoice.tenant.controller;


import com.app.invoice.tenant.dto.LedgerReport;
import com.app.invoice.tenant.dto.LedgerSummary;
import com.app.invoice.tenant.dto.LedgerTransactionResponse;
import com.app.invoice.tenant.service.LedgerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ledger")
@CrossOrigin(origins = "*", maxAge = 3600)
public class LedgerController {
    private final LedgerService ledgerService;

    public LedgerController(LedgerService ledgerService) {
        this.ledgerService = ledgerService;
    }

    @PostMapping
    public ResponseEntity<LedgerSummary> getLedgerSummary() {
        LedgerSummary summary =ledgerService.getLedgerSummary();
        return ResponseEntity.ok(summary);
    }
    @PostMapping("/transactions")
    public ResponseEntity<List<LedgerTransactionResponse>> getLedgerTransactions() {
        return ResponseEntity.ok(ledgerService.getLedgerTransactions());
    }
    @PostMapping ("/summary")
    public ResponseEntity<LedgerReport> getLedgerSummaryDetails() {
       return  ResponseEntity.ok(ledgerService.getLedgerWithSummary());
    }
}
