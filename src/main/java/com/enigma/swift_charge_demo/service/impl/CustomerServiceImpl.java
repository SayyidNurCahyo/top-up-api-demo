package com.enigma.swift_charge_demo.service.impl;

import com.enigma.swift_charge_demo.dto.request.SearchCustomerRequest;
import com.enigma.swift_charge_demo.dto.request.UpdateCustomerRequest;
import com.enigma.swift_charge_demo.dto.response.CustomerResponse;
import com.enigma.swift_charge_demo.entity.Customer;
import com.enigma.swift_charge_demo.repository.CustomerRepository;
import com.enigma.swift_charge_demo.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    @Override
    public void create(Customer customer) {
        customerRepository.saveAndFlush(customer);
    }

    @Override
    public CustomerResponse getById(String id) {
        return null;
    }

    @Override
    public Page<CustomerResponse> getAll(SearchCustomerRequest request) {
        return null;
    }

    @Override
    public CustomerResponse update(UpdateCustomerRequest request) {
        return null;
    }

    @Override
    public CustomerResponse deleteById(String id) {
        return null;
    }
}
