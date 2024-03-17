package com.enigma.swift_charge_demo.service;

import com.enigma.swift_charge_demo.dto.response.JwtResponse;
import com.enigma.swift_charge_demo.entity.UserAccount;

public interface JwtService {
    String generateToken(UserAccount account);
    Boolean verifyJwtToken(String token);
    JwtResponse getClaimsByToken(String token);
}
