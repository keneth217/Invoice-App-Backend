package com.app.invoice.tenant.dto.school_settings;

import lombok.Data;

import java.time.LocalDate;
import java.util.Base64;

@Data
public class PharmacySettingsDTO {
    private Long id;
    private String pharmacyId;
    private String pharmacyName;
    private String pharmacyCode;
    private String licenseNumber;
    private String address;
    private String email;
    private String phoneNumber;
    private boolean licenseValid;
    private LocalDate registrationDate;
    private LocalDate licenseExpiryDate;
    private String operatingHours;
    private boolean stockTrackingEnabled;
    private boolean salesTrackingEnabled;
    private boolean allowPrescriptionUploads;
    private int smsCredits;
    private byte[] pharmacyLogo;

    public String getPharmacyLogoBase64() {
        return pharmacyLogo != null ? Base64.getEncoder().encodeToString(pharmacyLogo) : null;
    }
}
