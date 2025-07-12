package com.app.invoice.tenant.controllers;

import com.app.invoice.tenant.dto.InvoiceRequest;
import com.app.invoice.tenant.dto.InvoiceResponse;
import com.app.invoice.tenant.services.InvoiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/v1/invoices")
@Tag(name = "Invoice Management", description = "APIs for invoice operations")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @Operation(
        summary = "Create a new invoice",
        description = "Creates a new invoice with items and associates it with a customer"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Invoice created successfully",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = InvoiceResponse.class),
                examples = @ExampleObject(
                    name = "Success Response",
                    value = """
                    {
                        "id": 1,
                        "invoiceNumber": "INV-0001",
                        "issueDate": "2024-01-15",
                        "dueDate": "2024-02-15",
                        "totalAmount": 1500.00,
                        "status": "UNPAID",
                        "customer": {
                            "id": 1,
                            "name": "John Doe",
                            "email": "john@example.com",
                            "phone": "+1234567890"
                        },
                        "invoiceItems": [
                            {
                                "id": 1,
                                "description": "Web Development Services",
                                "quantity": 10,
                                "unitPrice": 100.00,
                                "itemsTotal": 1000.00
                            },
                            {
                                "id": 2,
                                "description": "Design Services",
                                "quantity": 5,
                                "unitPrice": 100.00,
                                "itemsTotal": 500.00
                            }
                        ]
                    }
                    """
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "Invalid invoice data"),
        @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @PostMapping
    public ResponseEntity<InvoiceResponse> createInvoice(
        @Parameter(description = "Invoice details", required = true)
        @RequestBody InvoiceRequest invoice) {
        InvoiceResponse createdInvoice = invoiceService.createInvoice(invoice);
        return ResponseEntity.status(201).body(createdInvoice);
    }

    @Operation(
        summary = "Get all invoices",
        description = "Retrieves all invoices for the current tenant"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Invoices retrieved successfully",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = InvoiceResponse.class),
                examples = @ExampleObject(
                    name = "Success Response",
                    value = """
                    [
                        {
                            "id": 1,
                            "invoiceNumber": "INV-0001",
                            "issueDate": "2024-01-15",
                            "dueDate": "2024-02-15",
                            "totalAmount": 1500.00,
                            "status": "UNPAID",
                            "customer": {
                                "id": 1,
                                "name": "John Doe",
                                "email": "john@example.com",
                                "phone": "+1234567890"
                            }
                        },
                        {
                            "id": 2,
                            "invoiceNumber": "INV-0002",
                            "issueDate": "2024-01-16",
                            "dueDate": "2024-02-16",
                            "totalAmount": 2500.00,
                            "status": "PAID",
                            "customer": {
                                "id": 2,
                                "name": "Jane Smith",
                                "email": "jane@example.com",
                                "phone": "+1987654321"
                            }
                        }
                    ]
                    """
                )
            )
        )
    })
    @PostMapping("/all")
    public ResponseEntity<List<InvoiceResponse>> getAllInvoices() {
        List<InvoiceResponse> invoices = invoiceService.getAllInvoices();
        return ResponseEntity.ok(invoices);
    }
    @Operation(
        summary = "Update an invoice",
        description = "Updates an existing invoice with new details"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Invoice updated successfully",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = InvoiceResponse.class)
            )
        ),
        @ApiResponse(responseCode = "404", description = "Invoice not found"),
        @ApiResponse(responseCode = "400", description = "Invalid invoice data")
    })
    @PostMapping("/{id}/update")
    public ResponseEntity<InvoiceResponse> updateInvoice(
        @Parameter(description = "Invoice ID", required = true)
        @PathVariable Long id,
        @Parameter(description = "Updated invoice details", required = true)
        @RequestBody InvoiceRequest invoice) {
        InvoiceResponse updatedInvoice = invoiceService.updateInvoice(id, invoice);
        return ResponseEntity.ok(updatedInvoice);
    }

}
