package com.app.invoice.tenant.serviceImpl;

import com.app.invoice.exception.CustomerNotFoundException;

import com.app.invoice.tenant.dto.CustomerRequest;
import com.app.invoice.tenant.dto.CustomerResponse;
import com.app.invoice.tenant.entity.Customer;
import com.app.invoice.tenant.mapper.CustomerMapper;
import com.app.invoice.tenant.repos.CustomerRepository;
import com.app.invoice.tenant.services.CustomerService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }


    @Override
    public CustomerResponse createCustomer(CustomerRequest customerRequest) {
        Customer saved = customerRepository.save(CustomerMapper.toEntity(customerRequest));
        return CustomerMapper.toResponse(saved);
    }

    @Override
    public List<CustomerResponse> getAllCustomers() {
        List<Customer> customerList = customerRepository.findAllByDeletedFalse();
        return customerList.stream()
                .map(CustomerMapper::toResponse)
                .toList();
    }

    @Override
    public CustomerResponse getCustomerById(Long id) {
    Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));
       return   CustomerMapper.toResponse(customer);
    }

    @Override
    public CustomerResponse updateCustomer(Long id, CustomerRequest customerRequest) {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));

        existingCustomer.setName(customerRequest.getName());
        existingCustomer.setEmail(customerRequest.getEmail());
        existingCustomer.setPhone(customerRequest.getPhone());
        existingCustomer.setAddress(customerRequest.getAddress());
        existingCustomer.setCity(customerRequest.getCity());
        existingCustomer.setState(customerRequest.getState());
        existingCustomer.setCountry(customerRequest.getCountry());
        existingCustomer.setZipCode(customerRequest.getZipCode());

        Customer saved = customerRepository.save(existingCustomer);
        return CustomerMapper.toResponse(saved);
    }
    @Override
    public CustomerResponse deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));

        customer.setDeleted(true);
        Customer saved = customerRepository.save(customer);
        return CustomerMapper.toResponse(saved);
    }


}
