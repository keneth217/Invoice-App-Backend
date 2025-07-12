package com.app.invoice.master.controllers;

import com.app.invoice.configs.tenants.TenantDatabaseService;
import com.app.invoice.master.dto.BusinessDTO;
import com.app.invoice.master.entity.Business;
import com.app.invoice.master.services.BusinessService;
import com.app.invoice.tenant.dto.BusinessRequest;
import com.app.invoice.tenant.dto.BusinessResponse;
import com.app.invoice.tenant.services.defaultSettings.DefaultSettingsService;


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
public class BusinessController {

    @Autowired
    private BusinessService businessService;

    @Autowired
    private DefaultSettingsService defaultSettingsService;

    @Autowired
    private TenantDatabaseService tenantDatabaseService;

    private static final Logger log = LoggerFactory.getLogger(BusinessController.class);

    @PostMapping("/register")
    public ResponseEntity<BusinessResponse> registerBusiness(@RequestBody BusinessRequest request) {
        BusinessResponse response =businessService.createBusiness(request);
        return ResponseEntity.ok(response);
    }



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
