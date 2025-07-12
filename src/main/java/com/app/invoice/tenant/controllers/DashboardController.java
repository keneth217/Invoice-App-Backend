package com.app.invoice.tenant.controllers;

import com.app.invoice.tenant.dto.DashboardStatsResponse;
import com.app.invoice.tenant.services.DashboardService;
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

@RestController
@RequestMapping("/api/v1/stats")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Dashboard & Analytics", description = "APIs for dashboard statistics and analytics")
public class DashboardController {
    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @Operation(
        summary = "Get dashboard statistics",
        description = "Retrieves comprehensive dashboard statistics including totals, recent activity, and monthly trends"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Dashboard statistics retrieved successfully",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = DashboardStatsResponse.class),
                examples = @ExampleObject(
                    name = "Success Response",
                    value = """
                    {
                        "totalCustomers": 150,
                        "totalInvoices": 500,
                        "totalReceipts": 450,
                        "totalVouchers": 450,
                        "totalSales": 75000.00,
                        "totalPayments": 67500.00,
                        "totalExpenses": 0.0,
                        "totalProfit": 75000.00,
                        "recentInvoices": [
                            {
                                "id": 1,
                                "invoiceNumber": "INV-0500",
                                "issueDate": "2024-01-15",
                                "dueDate": "2024-02-15",
                                "totalAmount": 1500.00,
                                "status": "UNPAID"
                            }
                        ],
                        "recentReceipts": [
                            {
                                "id": 1,
                                "receiptNumber": "RCT-0450",
                                "issueDate": "2024-01-14",
                                "totalAmount": 1200.00
                            }
                        ],
                        "recentVouchers": [
                            {
                                "id": 1,
                                "voucherNumber": "VCH-0450",
                                "paymentDate": "2024-01-14",
                                "amountPaid": 1200.00
                            }
                        ],
                        "expiredInvoicesCount": 5,
                        "overdueInvoicesCount": 12,
                        "upcomingInvoicesCount": 25,
                        "monthlyStats": [
                            {
                                "month": "January",
                                "totalInvoice": 25000.00,
                                "totalExpense": 22500.00
                            }
                        ],
                        "recentTransactions": [
                            {
                                "transactionDate": "2024-01-15T10:30:00",
                                "transactionType": "INVOICE",
                                "referenceNumber": "INV-0500",
                                "debitAmount": 1500.00,
                                "creditAmount": 0.00,
                                "description": "Invoice created for customer John Doe",
                                "runningBalance": -1500.00
                            }
                        ]
                    }
                    """
                )
            )
        )
    })
    @PostMapping
    public ResponseEntity<DashboardStatsResponse> getDashboardStats() {
        DashboardStatsResponse response = dashboardService.getDashboardStats();
        return ResponseEntity.ok(response);
    }
}
