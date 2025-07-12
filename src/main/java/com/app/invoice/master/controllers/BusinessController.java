package com.app.invoice.master.controllers;

import com.app.invoice.configs.tenants.TenantDatabaseService;
import com.app.invoice.master.dto.BusinessDTO;
import com.app.invoice.master.entity.Business;
import com.app.invoice.master.services.BusinessService;
import com.app.invoice.tenant.dto.BusinessRequest;
import com.app.invoice.tenant.dto.BusinessResponse;
import com.app.invoice.tenant.services.defaultSettings.DefaultSettingsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/pharmacy")
@Tag(name = "Business Management", description = "APIs for business registration and management")
public class BusinessController {

    @Autowired
    private BusinessService businessService;

    @Autowired
    private DefaultSettingsService defaultSettingsService;

    @Autowired
    private TenantDatabaseService tenantDatabaseService;

    private static final Logger log = LoggerFactory.getLogger(BusinessController.class);

    @Operation(
        summary = "Register a new business",
        description = "Creates a new business account with a unique business code and sets up the tenant database"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Business registered successfully",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = BusinessResponse.class),
                examples = @ExampleObject(
                    name = "Success Response",
                    value = """
                    {
                        "id": "123e4567-e89b-12d3-a456-426614174000",
                        "name": "ABC Company",
                        "email": "contact@abc.com",
                        "phone": "+1234567890",
                        "businessCode": "BUS-0001",
                        "address": "123 Main St",
                        "city": "New York",
                        "state": "NY",
                        "country": "USA",
                        "zipCode": "10001",
                        "invoicePrefix": "INV-",
                        "currencySymbol": "USD"
                    }
                    """
                )
            )
        ),
        @ApiResponse(responseCode = "400", description = "Invalid business data"),
        @ApiResponse(responseCode = "409", description = "Business already exists")
    })
    @PostMapping("/register")
    public ResponseEntity<BusinessResponse> registerBusiness(
        @Parameter(description = "Business registration details", required = true)
        @RequestBody BusinessRequest request) {
        BusinessResponse response = businessService.createBusiness(request);
        return ResponseEntity.ok(response);
    }



    @Operation(
        summary = "Get all businesses",
        description = "Retrieves a list of all registered businesses"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of businesses retrieved successfully",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = BusinessResponse.class),
                examples = @ExampleObject(
                    name = "Success Response",
                    value = """
                    [
                        {
                            "id": "123e4567-e89b-12d3-a456-426614174000",
                            "name": "ABC Company",
                            "email": "contact@abc.com",
                            "phone": "+1234567890",
                            "businessCode": "BUS-0001",
                            "address": "123 Main St",
                            "city": "New York",
                            "state": "NY",
                            "country": "USA",
                            "zipCode": "10001",
                            "invoicePrefix": "INV-",
                            "currencySymbol": "USD"
                        },
                        {
                            "id": "456e7890-e89b-12d3-a456-426614174001",
                            "name": "XYZ Corporation",
                            "email": "info@xyz.com",
                            "phone": "+1987654321",
                            "businessCode": "BUS-0002",
                            "address": "456 Oak Ave",
                            "city": "Los Angeles",
                            "state": "CA",
                            "country": "USA",
                            "zipCode": "90210",
                            "invoicePrefix": "INV-",
                            "currencySymbol": "USD"
                        }
                    ]
                    """
                )
            )
        )
    })
    @PostMapping("/list")
    public List<BusinessResponse> listPharmacies() {
        return businessService.getAllCompanies();
    }

    @GetMapping("/{pharmacyID}")
    public ResponseEntity<Business> getPharmacyById(@PathVariable UUID businessID) {
        Business pharmacy = businessService.findByBusinessId(businessID);
        return pharmacy != null ? ResponseEntity.ok(pharmacy) : ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','DEPUTY_ADMIN','ADMIN','PRINCIPAL','DEPUTY_PRINCIPAL','FORM_SUPERVISOR')")
    @PutMapping("/{pharmacyID}")
    public ResponseEntity<BusinessResponse> updatePharmacy(@PathVariable UUID businessID, @RequestBody BusinessRequest businessRequest) {
        try {
            BusinessResponse pharmacy = businessService.updateCompany(businessID, businessRequest);
            return ResponseEntity.ok(pharmacy);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','DEPUTY_ADMIN','ADMIN')")
    @PutMapping("/{pharmacyID}/activate")
    public ResponseEntity<Business> activatePharmacy(@PathVariable String pharmacyID) {
        Business updatedPharmacy = businessService.activateBusiness(pharmacyID);
        return ResponseEntity.ok(updatedPharmacy);
    }

    @PutMapping("/{pharmacyID}/deactivate")
    public ResponseEntity<Business> deactivatePharmacy(@PathVariable String pharmacyID) {
        Business updatedPharmacy = businessService.deactivateBusiness(pharmacyID);
        return ResponseEntity.ok(updatedPharmacy);
    }

    @GetMapping("/activated")
    public ResponseEntity<List<BusinessResponse>> getActivatedPharmacies() {
        List<BusinessResponse> pharmacies = businessService.getActivatedBusinesses();
        return ResponseEntity.ok(pharmacies);
    }

    @GetMapping("/deactivated")
    public ResponseEntity<List<Business>> getDeactivatedPharmacies() {
        List<Business> pharmacies = businessService.getDeactivatedBusinesses();
        return ResponseEntity.ok(pharmacies);
    }

    @GetMapping("/counts")
    public ResponseEntity<Map<String, Long>> getPharmacyCounts() {
        Map<String, Long> counts = businessService.getBusinessCounts();
        return ResponseEntity.ok(counts);
    }
}
