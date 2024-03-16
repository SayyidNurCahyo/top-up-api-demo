package com.enigma.swift_charge_demo.service.impl;

import com.enigma.swift_charge_demo.dto.request.LoginRequest;
import com.enigma.swift_charge_demo.dto.request.RegisterRequest;
import com.enigma.swift_charge_demo.dto.response.LoginResponse;
import com.enigma.swift_charge_demo.dto.response.RegisterResponse;
import com.enigma.swift_charge_demo.repository.UserAccountRepository;
import com.enigma.swift_charge_demo.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;


    @Override
    public RegisterResponse register(RegisterRequest request) {
        return null;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        return null;
    }
}
