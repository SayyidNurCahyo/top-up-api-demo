package com.enigma.swift_charge_demo.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewTransactionRequest {
    private String customerId;
    private String productId;
}
