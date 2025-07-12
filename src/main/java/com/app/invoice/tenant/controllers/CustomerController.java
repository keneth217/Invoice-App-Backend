package com.app.invoice.tenant.controllers;

import com.app.invoice.tenant.dto.CustomerRequest;
import com.app.invoice.tenant.dto.CustomerResponse;
import com.app.invoice.tenant.services.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Customer Management", description = "APIs for customer operations")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }
    @Operation(
        summary = "Create a new customer",
        description = "Creates a new customer for the current tenant"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Customer created successfully",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = CustomerResponse.class),
                examples = @ExampleObject(
                    name = "Success Response",
                    value = """
                    {
                        "id": 1,
                        "name": "John Doe",
                        "email": "john@example.com",
                        "phone": "+1234567890",
                        "address": "123 Main St",
                        "city": "New York",
                        "state": "NY",
                        "country": "USA",
                        "zipCode": "10001"
                    }
                    """
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "Invalid customer data"),
        @ApiResponse(responseCode = "409", description = "Customer already exists")
    })
    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(
        @Parameter(description = "Customer details", required = true)
        @RequestBody CustomerRequest customerRequest) {
        CustomerResponse response = customerService.createCustomer(customerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/all")
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        List<CustomerResponse> response = customerService.getAllCustomers();
        return ResponseEntity.ok(response);
    }
    @PostMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomerById(@RequestBody Long id) {
        CustomerResponse response = customerService.getCustomerById(id);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/{id}/update")
    public ResponseEntity<CustomerResponse> updateCustomer(@RequestBody Long id, @RequestBody CustomerRequest customerRequest) {
        CustomerResponse response = customerService.updateCustomer(id, customerRequest);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/{id}/delete")
    public ResponseEntity<CustomerResponse> deleteCustomer(@RequestBody Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

}
