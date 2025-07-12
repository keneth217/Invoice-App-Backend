package com.app.invoice.master.services;

import com.app.invoice.master.entity.Business;
import com.app.invoice.tenant.dto.BusinessRequest;
import com.app.invoice.tenant.dto.BusinessResponse;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;


public interface BusinessService {
    BusinessResponse createBusiness(BusinessRequest businessRequest);


    List<BusinessResponse> getAllCompanies();
    BusinessResponse getBusinessDetails();
    BusinessResponse getCompanyById(UUID id);
    BusinessResponse updateCompany(UUID id, BusinessRequest businessRequest);
    BusinessResponse deleteCompany(UUID id);
    BusinessResponse getCompanyByCode(String businessCode);
    BusinessResponse updateBusinessSettings(UUID id, BusinessRequest businessRequest);

    BusinessResponse getCurrentSettings();

    Business findByBusinessId(UUID id);

    Business findByBusinessCode(String businessCode);


    List<BusinessResponse> getActivatedBusinesses();

    Business deactivateBusiness(String businessID);

    Business activateBusiness(String businessID);

    List<Business> getDeactivatedBusinesses();

    Map<String, Long> getBusinessCounts();

}
