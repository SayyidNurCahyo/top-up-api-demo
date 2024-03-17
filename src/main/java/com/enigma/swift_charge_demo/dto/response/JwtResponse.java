package com.enigma.swift_charge_demo.dto.response;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
    private String userAccountId;
    private List<String> roles;
}
