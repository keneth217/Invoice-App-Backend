package com.app.invoice.tenant.services.BusinessDetails;

import com.app.invoice.tenant.dto.school_settings.PharmacyNameDTO;
import com.app.invoice.tenant.dto.school_settings.PharmacySettingsDTO;
import com.app.invoice.tenant.entity.school_settings.BusinessSettings;

import org.springframework.web.multipart.MultipartFile;

public interface BusinessSettingsService {
    PharmacySettingsDTO getPharmacySettings();

    PharmacySettingsDTO updatePharmacySettings(PharmacySettingsDTO pharmacySettingsDTO, MultipartFile pharmacyLogo);

    PharmacySettingsDTO createPharmacySettings(PharmacySettingsDTO pharmacySettingsDTO);

    PharmacyNameDTO getPharmacyName();

    BusinessSettings updatePharmacyLogo(byte[] logo);
}
