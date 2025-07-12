package com.app.invoice.tenant.controllers.school_settings;

import com.app.invoice.tenant.dto.school_settings.PharmacyNameDTO;
import com.app.invoice.tenant.dto.school_settings.PharmacySettingsDTO;
import com.app.invoice.tenant.services.BusinessDetails.BusinessSettingsService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/pharmacy/settings")
@CrossOrigin(origins = "*", maxAge = 3600)
public class BusinessSettingsController {

    private final BusinessSettingsService pharmacySettingsService;

    private static final Logger logger = LoggerFactory.getLogger(BusinessSettingsController.class);

    @Autowired
    public BusinessSettingsController(BusinessSettingsService pharmacySettingsService) {
        this.pharmacySettingsService = pharmacySettingsService;
    }

    @GetMapping
    public PharmacySettingsDTO getPharmacySettings() {
        return pharmacySettingsService.getPharmacySettings();
    }

    @PreAuthorize("hasAnyAuthority('OWNER','MANAGER','PHARMACIST')")
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public PharmacySettingsDTO updatePharmacySettings(
            @RequestPart("data") PharmacySettingsDTO pharmacySettingsDTO,
            @RequestPart(value = "pharmacyLogo", required = false) MultipartFile pharmacyLogo
    ) throws IOException {
        return pharmacySettingsService.updatePharmacySettings(pharmacySettingsDTO, pharmacyLogo);
    }

    @PreAuthorize("hasAnyAuthority('OWNER','MANAGER')")
    @PostMapping
    public PharmacySettingsDTO createPharmacySettings(@RequestBody PharmacySettingsDTO pharmacySettingsDTO) {
        return pharmacySettingsService.createPharmacySettings(pharmacySettingsDTO);
    }

    @GetMapping("/name")
    public ResponseEntity<PharmacyNameDTO> getPharmacyName() {
        PharmacyNameDTO pharmacyName = pharmacySettingsService.getPharmacyName();
        return ResponseEntity.ok(pharmacyName);
    }

    @PutMapping("/update-logo")
    public ResponseEntity<String> updatePharmacyLogo(@RequestParam("logo") MultipartFile logo) {
        try {
            byte[] logoBytes = logo.getBytes();
            pharmacySettingsService.updatePharmacyLogo(logoBytes);
            return new ResponseEntity<>("Business logo successfully updated.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update logo: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
