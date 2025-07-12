package com.app.invoice.tenant.service;



import com.app.invoice.master.entity.Business;
import com.app.invoice.tenant.dto.BusinessRequest;
import com.app.invoice.tenant.dto.BusinessResponse;

import java.util.List;
import java.util.Map;

public interface BusinessService {
    BusinessResponse createCompany(BusinessRequest businessRequest);
    List<BusinessResponse> getAllCompanies();
    BusinessResponse getBusinessDetails();
    BusinessResponse getCompanyById(Long id);
    BusinessResponse updateCompany(Long id, BusinessRequest businessRequest);
    BusinessResponse deleteCompany(Long id);
    BusinessResponse getCompanyByCode(String businessCode);
    BusinessResponse updateBusinessSettings(Long id, BusinessRequest businessRequest);

    BusinessResponse getCurrentSettings();

    Business findByBusinessId(String businessId);


    List<BusinessResponse> getActivatedBusinesses();

    Business deactivateBusiness(String businessID);

    Business activateBusiness(String businessID);

    List<Business> getDeactivatedBusinesses();

    Map<String, Long> getBusinessCounts();
}
