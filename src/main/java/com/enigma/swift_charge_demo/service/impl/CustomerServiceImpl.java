package com.enigma.swift_charge_demo.service.impl;

import com.enigma.swift_charge_demo.dto.request.SearchCustomerRequest;
import com.enigma.swift_charge_demo.dto.request.UpdateCustomerRequest;
import com.enigma.swift_charge_demo.dto.response.CustomerResponse;
import com.enigma.swift_charge_demo.entity.Customer;
import com.enigma.swift_charge_demo.entity.UserAccount;
import com.enigma.swift_charge_demo.repository.CustomerRepository;
import com.enigma.swift_charge_demo.repository.UserAccountRepository;
import com.enigma.swift_charge_demo.service.CustomerService;
import com.enigma.swift_charge_demo.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final ValidationUtil validationUtil;
    private final UserAccountRepository userAccountRepository;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void create(Customer customer) {
        customerRepository.saveAndFlush(customer);
    }

    @Transactional(readOnly = true)
    @Override
    public CustomerResponse getById(String id) {
        Customer customer = customerRepository.findByIdEnable(id).orElseThrow(()->new RuntimeException("customer not found"));
        return convertToCustomerResponse(customer);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<CustomerResponse> getAll(SearchCustomerRequest request) {
        if (request.getPage()<1) request.setPage(1);
        if (request.getSize()<1) request.setSize(1);
        Pageable page = PageRequest.of(request.getPage() -1, request.getSize(), Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy()));
        if(request.getName()!=null){
            Page<Customer> customers = customerRepository.findCustomer(request.getName(), page).orElseThrow(()->new RuntimeException("customer not found"));
            return convertToPageCustomerResponse(customers);
        }else {
            return convertToPageCustomerResponse(customerRepository.findAllCustomer(page));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CustomerResponse update(UpdateCustomerRequest request) {
        validationUtil.validate(request);
        Customer customer = customerRepository.findByIdEnable(request.getId()).orElseThrow(()->new RuntimeException("customer not found"));
        customer.setName(request.getName());
        customer.setPhone(request.getPhone());
        UserAccount account = customer.getUserAccount();
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        account.setIsEnabled(true);
        customer.setUserAccount(account);
        customerRepository.saveAndFlush(customer);
        return convertToCustomerResponse(customer);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(String id) {
        Customer customer = customerRepository.findByIdEnable(id).orElseThrow(()->new RuntimeException("customer not found"));
        userAccountRepository.disableAccount(customer.getUserAccount().getId());
    }

    private CustomerResponse convertToCustomerResponse(Customer customer) {
        return CustomerResponse.builder().customerId(customer.getId()).customerName(customer.getName())
                .customerPhone(customer.getPhone()).customerEmail(customer.getUserAccount().getEmail())
                .isEnabled(customer.getUserAccount().getIsEnabled()).build();
    }

    private Page<CustomerResponse> convertToPageCustomerResponse(Page<Customer> customers) {
        return customers.map(this::convertToCustomerResponse);
    }
}
