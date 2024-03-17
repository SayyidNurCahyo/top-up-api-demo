package com.enigma.swift_charge_demo.service;

import com.enigma.swift_charge_demo.entity.UserAccount;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserAccount getByUserId(String id);
    UserAccount getByContext();
}
