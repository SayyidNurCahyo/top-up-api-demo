package com.enigma.swift_charge_demo.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerResponse {
    private String customerId;
    private String customerName;
    private String customerPhone;
    private String customerEmail;
    private Boolean isEnabled;
}
