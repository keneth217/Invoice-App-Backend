package com.app.invoice.tenant.repos.business_settings;

import com.app.invoice.tenant.entity.school_settings.BusinessSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BusinessSettingsRepository extends JpaRepository<BusinessSettings, Long> {
 ;

    Optional<BusinessSettings> findFirstByOrderByIdDesc();

    Optional<BusinessSettings> findTopByOrderByIdAsc();

    Optional<BusinessSettings> findByPharmacyId(String pharmacyId);
}