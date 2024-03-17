package com.enigma.swift_charge_demo.security;

import com.enigma.swift_charge_demo.dto.response.CustomerResponse;
import com.enigma.swift_charge_demo.entity.UserAccount;
import com.enigma.swift_charge_demo.service.CustomerService;
import com.enigma.swift_charge_demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@RequiredArgsConstructor
public class AuthenticatedUser {
    private final CustomerService customerService;
    private final UserService userService;
    public boolean hasId(String customerId){
        CustomerResponse customerResponse = customerService.getById(customerId);
        UserAccount customerAccount = (UserAccount) userService.loadUserByUsername(customerResponse.getCustomerEmail());
        UserAccount userAccount = userService.getByContext();
        if (!userAccount.getId().equals(customerAccount.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "forbidden, access denied");
        }
        return true;
    }
}
