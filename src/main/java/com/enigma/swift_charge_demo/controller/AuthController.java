package com.enigma.swift_charge_demo.controller;

import com.enigma.swift_charge_demo.constant.APIUrl;
import com.enigma.swift_charge_demo.dto.request.LoginRequest;
import com.enigma.swift_charge_demo.dto.request.RegisterRequest;
import com.enigma.swift_charge_demo.dto.response.CommonResponse;
import com.enigma.swift_charge_demo.dto.response.LoginResponse;
import com.enigma.swift_charge_demo.dto.response.RegisterResponse;
import com.enigma.swift_charge_demo.service.AuthService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = APIUrl.AUTH)
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class AuthController {
    private final AuthService authService;

    @PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<RegisterResponse>> register(@RequestBody RegisterRequest request){
        RegisterResponse response = authService.register(request);
        CommonResponse<RegisterResponse> commonResponse = CommonResponse.<RegisterResponse>builder()
                .statusCode(HttpStatus.CREATED.value()).message("account created successfully")
                .data(response).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(commonResponse);
    }

    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommonResponse<LoginResponse>> login(@RequestBody LoginRequest request){
        LoginResponse response = authService.login(request);
        CommonResponse<LoginResponse> commonResponse = CommonResponse.<LoginResponse>builder()
                .statusCode(HttpStatus.OK.value()).message("login successfully")
                .data(response).build();
        return ResponseEntity.ok(commonResponse);
    }
}
