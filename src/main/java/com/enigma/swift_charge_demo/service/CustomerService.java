package com.enigma.swift_charge_demo.service;

import com.enigma.swift_charge_demo.dto.request.SearchCustomerRequest;
import com.enigma.swift_charge_demo.dto.request.UpdateCustomerRequest;
import com.enigma.swift_charge_demo.dto.response.CustomerResponse;
import com.enigma.swift_charge_demo.entity.Customer;
import org.springframework.data.domain.Page;

public interface CustomerService {
    void create(Customer customer);
    CustomerResponse getById(String id);
    Page<CustomerResponse> getAll(SearchCustomerRequest request);
    CustomerResponse update(UpdateCustomerRequest request);
    void deleteById(String id);
}
