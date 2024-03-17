package com.enigma.swift_charge_demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequest {
    @NotBlank(message = "email is required")
    private String email;
    @NotBlank(message = "Password is Required")
    private String password;
}
