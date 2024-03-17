package com.enigma.swift_charge_demo.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionResponse {
    private String id;
    private String date;
    private String customerName;
    private String customerEmail;
    private String productName;
    private Long productPrice;
    private String paymentId;
    private String redirectUrl;
}
