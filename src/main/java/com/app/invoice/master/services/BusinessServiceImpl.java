package com.app.invoice.master.services;

import com.app.invoice.configs.tenants.TenantDatabaseService;
import com.app.invoice.exception.InvalidBusinessException;
import com.app.invoice.exception.NotFoundException;
import com.app.invoice.master.entity.Business;
import com.app.invoice.master.repos.BusinessRepository;
import com.app.invoice.tenant.dto.BusinessRequest;
import com.app.invoice.tenant.dto.BusinessResponse;
import com.app.invoice.tenant.mapper.BusinessMapper;
import com.app.invoice.utils.BusinessCodeGenerator;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
@Service
public class BusinessServiceImpl implements BusinessService {

    private final BusinessRepository businessRepository;
    private final BusinessCodeGenerator businessCodeGenerator;
    private static final Logger log = LoggerFactory.getLogger(BusinessServiceImpl.class);
    @Autowired
    @Lazy
    private TenantDatabaseService tenantDatabaseService;

    public BusinessServiceImpl(BusinessRepository businessRepository, BusinessCodeGenerator businessCodeGenerator) {
        this.businessRepository = businessRepository;
        this.businessCodeGenerator = businessCodeGenerator;
    }
    @Override
    @Transactional(transactionManager = "centralTransactionManager")
    public BusinessResponse createBusiness(BusinessRequest businessRequest) {
        // Validate input
        if (businessRequest == null ||
                StringUtils.isBlank(businessRequest.getName()) ||
                StringUtils.isBlank(businessRequest.getEmail())) {
            throw new InvalidBusinessException("Valid business name and email are required to register");
        }

        // Generate business code
        String code = businessCodeGenerator.generate();
        log.debug("Generated business code: {}", code);

        // Set default values
        businessRequest.setBusinessCode(code);
        businessRequest.setInvoicePrefix("INV-");
        businessRequest.setReceiptPrefix("RCT-");
        businessRequest.setVoucherPrefix("VCH-");
        businessRequest.setCurrencySymbol("KES");
        businessRequest.setDeleted(false);
        businessRequest.setInvoiceFooter("Thank you for your business!");
        businessRequest.setInvoiceTerms("Payment is due within 30 days.");
        businessRequest.setInvoiceNote("If you have any questions, please contact us.");
        businessRequest.setReceiptFooter("Thank you for your payment!");
        businessRequest.setReceiptTerms("Payment is due within 30 days.");
        businessRequest.setReceiptNote("If you have any questions, please contact us.");
        businessRequest.setWebsite("https://www.example.com");

        // Check for existing business
        if (businessRepository.existsByBusinessCode(code)) {
            throw new InvalidBusinessException("Business code already exists. Please try again.");
        }

        if (businessRepository.existsByName(businessRequest.getName())) {
            throw new InvalidBusinessException("A business with this name already exists. Please use a different name or login.");
        }



        // Save business
        Business business = businessRepository.save(BusinessMapper.toCompanyEntity(businessRequest));
        log.info("Business created successfully with ID: {}", business.getId());

        // Initiate async database creation
        tenantDatabaseService.createDatabase(business)
                .thenAccept(result -> log.info("Database created successfully for business {}", business.getId()))
                .exceptionally(ex -> {
                    log.error("Failed to create database for business {}", business.getId(), ex);
                    return null;
                });

        return BusinessMapper.toCompanyResponse(business);
    }

    @Override
    public List<BusinessResponse> getAllCompanies() {
        // Fetch all businesses from the repository not deleted
        List<Business> businessList = businessRepository.findAllByDeletedFalse();
        return businessList.stream()
                .map(BusinessMapper::toCompanyResponse)
                .toList();
    }

    @Override
    public BusinessResponse getBusinessDetails() {
        // Fetch the first business that is not deleted
        Business business = businessRepository.findByDeletedFalse()
                .orElseThrow(() -> new NotFoundException("No active business found"));
        return BusinessMapper.toCompanyResponse(business);

    }

    @Override
    public BusinessResponse getCompanyById(UUID id) {
        Business business = businessRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Company not found with id: " + id));
        return BusinessMapper.toCompanyResponse(business);
    }

    @Override
    public BusinessResponse updateCompany(UUID id, BusinessRequest businessRequest) {
        Business existingBusiness = businessRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Company not found with id: " + id));
        // Update the business entity with new values
        existingBusiness.setName(businessRequest.getName());
        existingBusiness.setEmail(businessRequest.getEmail());
        existingBusiness.setPhone(businessRequest.getPhone());
        existingBusiness.setAddress(businessRequest.getAddress());
        existingBusiness.setCity(businessRequest.getCity());
        existingBusiness.setState(businessRequest.getState());
        existingBusiness.setZipCode(businessRequest.getZipCode());
        existingBusiness.setCountry(businessRequest.getCountry() != null ? businessRequest.getCountry() : "Kenya");
        existingBusiness.setImageUrl(businessRequest.getImageUrl());
        // Save the updated business entity
        businessRepository.save(existingBusiness);
        return BusinessMapper.toCompanyResponse(existingBusiness);
    }

    @Override
    public BusinessResponse deleteCompany(UUID id) {
        Business business = businessRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Company not found"));
        //set the deleted flag to true
        business.setDeleted(true);
        businessRepository.save(business);
        return BusinessMapper.toCompanyResponse(business);

    }

    @Override
    public BusinessResponse getCompanyByCode(String businessCode) {
        Business business = businessRepository.findByBusinessCode(businessCode)
                .orElseThrow(() -> new NotFoundException("Company not found with code: " + businessCode));
        return BusinessMapper.toCompanyResponse(business);
    }

    @Override
    public BusinessResponse updateBusinessSettings(UUID id, BusinessRequest businessRequest) {
        Business existingBusinessSettings = businessRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Company not found with id: " + id));
        // Update the business settings with new values
        existingBusinessSettings.setCurrency(businessRequest.getCurrencySymbol());
        existingBusinessSettings.setInvoicePrefix(businessRequest.getInvoicePrefix());
        existingBusinessSettings.setInvoiceFooter(businessRequest.getInvoiceFooter());
        existingBusinessSettings.setInvoiceTerms(businessRequest.getInvoiceTerms());
        existingBusinessSettings.setInvoiceNote(businessRequest.getInvoiceNote());
        existingBusinessSettings.setReceiptPrefix(businessRequest.getReceiptPrefix());
        existingBusinessSettings.setReceiptFooter(businessRequest.getReceiptFooter());
        existingBusinessSettings.setReceiptTerms(businessRequest.getReceiptTerms());
        existingBusinessSettings.setReceiptNote(businessRequest.getReceiptNote());
        // Save the updated business entity
        businessRepository.save(existingBusinessSettings);

        return BusinessMapper.toCompanyResponse(existingBusinessSettings);
    }

    @Override
    public BusinessResponse getCurrentSettings() {

        Business business = businessRepository.findByDeletedFalse()
                .orElseThrow(() -> new NotFoundException("No active business found"));
        return BusinessMapper.toCompanyResponse(business);

    }

    @Override
    public Business findByBusinessId(UUID id) {
        try {

            return businessRepository.findById(id)
                    .orElse(null);
        } catch (NumberFormatException e) {
            // If businessId is not a number, try to find by business code
            return businessRepository.findByBusinessCode(String.valueOf(id))
                    .orElse(null);
        }
    }

    @Override
    public Business findByBusinessCode(String businessCode) {
        return businessRepository.findByBusinessCode(businessCode)
                .orElse(null);
    }

    @Override
    public List<BusinessResponse> getActivatedBusinesses() {
        return List.of();
    }

    @Override
    public Business deactivateBusiness(String businessID) {
        return null;
    }

    @Override
    public Business activateBusiness(String businessID) {
        return null;
    }

    @Override
    public List<Business> getDeactivatedBusinesses() {
        return List.of();
    }

    @Override
    public Map<String, Long> getBusinessCounts() {
        return Map.of();
    }



    private final Cache<String, Business> pharmacyNameCache = Caffeine.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(1, TimeUnit.HOURS)
            .build();

    @Transactional(readOnly = true, value = "centralTransactionManager")
    public Optional<Business> findByPharmacyName(String name) {
        return Optional.ofNullable(pharmacyNameCache.get(name.toLowerCase(),
                n -> businessRepository.findByNameIgnoreCase(n).orElse(null)
        ));
    }

    @Transactional(transactionManager = "centralTransactionManager")


    public Business findById(UUID id) {
        return businessRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Business not found with ID: " + id));
    }



    @Transactional(transactionManager = "centralTransactionManager")
    public Business activatePharmacy(UUID id) {
        Business pharmacy = findById(id);
        pharmacy.setActivated(true);
        return businessRepository.save(pharmacy);
    }

    @Transactional(transactionManager = "centralTransactionManager")
    public Business deactivatePharmacy(UUID id) {
        Business pharmacy = findById(id);
        pharmacy.setActivated(false);
        return businessRepository.save(pharmacy);
    }

    @Transactional(readOnly = true, value = "centralTransactionManager")
    public List<Business> getActivatedPharmacies() {
        return businessRepository.findByActivatedTrue();
    }

    @Transactional(readOnly = true, value = "centralTransactionManager")
    public List<Business> getDeactivatedPharmacies() {
        return businessRepository.findByActivatedFalse();
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void updatePharmacyStatus() {
        LocalDateTime now = LocalDateTime.now();
        List<Business> pharmacies = businessRepository.findAll();

        for (Business pharmacy : pharmacies) {
            if (pharmacy.getExpiryDate() != null) {
                boolean isExpired = !pharmacy.getExpiryDate().isAfter(Instant.from(now));
                if (pharmacy.getActivated() == isExpired) {
                    pharmacy.setActivated(!isExpired);
                    businessRepository.save(pharmacy);
                    System.out.println("Updated status for: " + pharmacy.getName() + " to " + pharmacy.getActivated());
                }
            }
        }
        System.out.println("Business status update task completed.");
    }

    @Transactional(transactionManager = "centralTransactionManager")
    public Business updatePharmacy(UUID id, Business updated) {
        Business existing = findById(id);

        existing.setName(Optional.ofNullable(updated.getName()).orElse(existing.getName()));
        existing.setBusinessCode(Optional.ofNullable(updated.getBusinessCode()).orElse(existing.getBusinessCode()));
        existing.setAddress(Optional.ofNullable(updated.getAddress()).orElse(existing.getAddress()));
        existing.setEmail(Optional.ofNullable(updated.getEmail()).orElse(existing.getEmail()));
        existing.setPhone(Optional.ofNullable(updated.getPhone()).orElse(existing.getPhone()));
        existing.setBusinessOwner(Optional.ofNullable(updated.getBusinessOwner()).orElse(existing.getBusinessOwner()));
        existing.setDbUrl(Optional.ofNullable(updated.getDbUrl()).orElse(existing.getDbUrl()));

        return businessRepository.save(existing);
    }

    @Transactional(readOnly = true, value = "centralTransactionManager")
    public Map<String, Long> getPharmacyCounts() {
        Map<String, Long> counts = new HashMap<>();
        counts.put("totalPharmacies", businessRepository.count());
        counts.put("activatedPharmacies", businessRepository.countByActivatedTrue());
        counts.put("deactivatedPharmacies", businessRepository.countByActivatedFalse());
        return counts;
    }



}
