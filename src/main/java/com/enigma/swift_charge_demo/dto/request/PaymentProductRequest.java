package com.enigma.swift_charge_demo.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentProductRequest {
    private String name;
    private Long price;
}
