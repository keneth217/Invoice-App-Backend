package com.app.invoice.tenant.services;



import com.app.invoice.tenant.dto.CustomerRequest;
import com.app.invoice.tenant.dto.CustomerResponse;

import java.util.List;

public interface CustomerService {
    CustomerResponse createCustomer(CustomerRequest customerRequest);
    List<CustomerResponse> getAllCustomers();
    CustomerResponse getCustomerById(Long id);
    CustomerResponse updateCustomer(Long id, CustomerRequest customerRequest);
    CustomerResponse deleteCustomer(Long id);

}
