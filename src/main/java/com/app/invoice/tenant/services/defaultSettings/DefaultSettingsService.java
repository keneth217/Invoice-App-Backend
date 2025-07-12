package com.app.invoice.tenant.services.defaultSettings;


import com.app.invoice.tenant.entity.business_settings.BusinessSettings;
import com.app.invoice.tenant.repos.business_settings.BusinessSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class DefaultSettingsService {

    @Autowired
    private BusinessSettingsRepository pharmacySettingsRepository;

    @Transactional(transactionManager = "tenantTransactionManager")
    public void applyDefaultSettings(String pharmacyId, String pharmacyName, String email, String pharmacyCode, String phoneNumber, String address) {
        pharmacySettingsRepository.findByPharmacyId(pharmacyId).orElseGet(() -> {
            BusinessSettings defaultSettings = new BusinessSettings();
            defaultSettings.setPharmacyId(pharmacyId);
            defaultSettings.setPharmacyName(pharmacyName);
            defaultSettings.setEmail(email);
            defaultSettings.setPharmacyCode(pharmacyCode);
            defaultSettings.setPhoneNumber(phoneNumber);
            defaultSettings.setAddress(address);

            // Default values
            defaultSettings.setLicenseValid(true);
            defaultSettings.setSmsCredits(100);
            defaultSettings.setStockTrackingEnabled(true);
            defaultSettings.setSalesTrackingEnabled(true);
            defaultSettings.setAllowPrescriptionUploads(true);
            defaultSettings.setRegistrationDate(LocalDate.now());
            defaultSettings.setLicenseExpiryDate(LocalDate.now().plusYears(1));
            defaultSettings.setPharmacyLogo(null); // Default logo can be added later

            return pharmacySettingsRepository.save(defaultSettings);
        });
    }
}
