package com.enigma.swift_charge_demo.service;

import com.enigma.swift_charge_demo.dto.request.LoginRequest;
import com.enigma.swift_charge_demo.dto.request.RegisterRequest;
import com.enigma.swift_charge_demo.dto.response.LoginResponse;
import com.enigma.swift_charge_demo.dto.response.RegisterResponse;

public interface AuthService {
    RegisterResponse register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
}
