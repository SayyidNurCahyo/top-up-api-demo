package com.enigma.swift_charge_demo.service;

import com.enigma.swift_charge_demo.dto.request.SearchCustomerRequest;
import com.enigma.swift_charge_demo.dto.response.CustomerResponse;
import com.enigma.swift_charge_demo.entity.Customer;
import org.springframework.data.domain.Page;

public interface CustomerService {
    void create(Customer customer);
    CustomerResponse getById(String id);
    Page<CustomerResponse> getAll(SearchCustomerRequest request);
    CustomerResponse updateEnable(String id);
    void deleteById(String id);
}
