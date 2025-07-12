package com.app.invoice.tenant.services.BusinessDetails;

import com.app.invoice.master.repos.BusinessRepository;
import com.app.invoice.tenant.dto.school_settings.PharmacyNameDTO;
import com.app.invoice.tenant.dto.school_settings.PharmacySettingsDTO;
import com.app.invoice.tenant.entity.business_settings.BusinessSettings;
import com.app.invoice.tenant.repos.business_settings.BusinessSettingsRepository;
import com.app.invoice.tenant.services.defaultSettings.DefaultSettingsService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class BusinessSettingsServiceImpl implements BusinessSettingsService {

    private static final Logger logger = LoggerFactory.getLogger(BusinessSettingsServiceImpl.class);

    private final BusinessSettingsRepository pharmacySettingsRepository;

    @Autowired
    private DefaultSettingsService defaultSettingsService;

    @Autowired
    private BusinessRepository pharmacyRepository;

    @Autowired
    public BusinessSettingsServiceImpl(BusinessSettingsRepository pharmacySettingsRepository) {
        this.pharmacySettingsRepository = pharmacySettingsRepository;
    }

    @PostConstruct
    public void init() {
        defaultSettingsService.applyDefaultSettings("DEFAULT_PHARMACY_ID", "PharmaMaster", "", "", "", "");
    }

    @Transactional(readOnly = true, value = "tenantTransactionManager")
    @Override
    public PharmacySettingsDTO getPharmacySettings() {
        BusinessSettings entity = pharmacySettingsRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Business settings not found"));
        return mapToDTO(entity);
    }

    @Transactional(transactionManager = "tenantTransactionManager")
    @Override
    public PharmacySettingsDTO updatePharmacySettings(PharmacySettingsDTO dto, MultipartFile logoFile) {
        BusinessSettings existing = pharmacySettingsRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Business settings not found"));

        existing.setPharmacyName(dto.getPharmacyName());
        existing.setLicenseNumber(dto.getLicenseNumber());
        existing.setEmail(dto.getEmail());
        existing.setPhoneNumber(dto.getPhoneNumber());
        existing.setAddress(dto.getAddress());
        existing.setOperatingHours(dto.getOperatingHours());
        existing.setLicenseExpiryDate(dto.getLicenseExpiryDate());
        // More fields as needed

        if (logoFile != null && !logoFile.isEmpty()) {
            try {
                existing.setPharmacyLogo(logoFile.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Failed to process pharmacy logo", e);
            }
        } else if (dto.getPharmacyLogo() != null) {
            existing.setPharmacyLogo(dto.getPharmacyLogo());
        }

        BusinessSettings updated = pharmacySettingsRepository.save(existing);
        return mapToDTO(updated);
    }

    @Transactional(transactionManager = "tenantTransactionManager")
    @Override
    public PharmacySettingsDTO createPharmacySettings(PharmacySettingsDTO dto) {
        BusinessSettings entity = mapToEntity(dto);
        BusinessSettings saved = pharmacySettingsRepository.save(entity);
        return mapToDTO(saved);
    }

    @Override
    public PharmacyNameDTO getPharmacyName() {
        
        return null;
    }

    @Transactional(transactionManager = "tenantTransactionManager")
    @Override
    public BusinessSettings updatePharmacyLogo(byte[] logo) {
        if (logo == null || logo.length == 0) {
            throw new IllegalArgumentException("Logo data must not be null or empty");
        }

        BusinessSettings settings = pharmacySettingsRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new IllegalStateException("Business settings not found"));

        settings.setPharmacyLogo(logo);
        return pharmacySettingsRepository.save(settings);
    }

    private PharmacySettingsDTO mapToDTO(BusinessSettings entity) {
        PharmacySettingsDTO dto = new PharmacySettingsDTO();
        dto.setId(entity.getId());
        dto.setPharmacyName(entity.getPharmacyName());
        dto.setLicenseNumber(entity.getLicenseNumber());
        dto.setEmail(entity.getEmail());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setAddress(entity.getAddress());
        dto.setOperatingHours(entity.getOperatingHours());
        dto.setLicenseExpiryDate(entity.getLicenseExpiryDate());
        dto.setPharmacyLogo(entity.getPharmacyLogo());
        return dto;
    }

    private BusinessSettings mapToEntity(PharmacySettingsDTO dto) {
        BusinessSettings entity = new BusinessSettings();
        entity.setId(dto.getId());
        entity.setPharmacyName(dto.getPharmacyName());
        entity.setLicenseNumber(dto.getLicenseNumber());
        entity.setEmail(dto.getEmail());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setAddress(dto.getAddress());
        entity.setOperatingHours(dto.getOperatingHours());
        entity.setLicenseExpiryDate(dto.getLicenseExpiryDate());
        entity.setPharmacyLogo(dto.getPharmacyLogo());
        return entity;
    }
}
