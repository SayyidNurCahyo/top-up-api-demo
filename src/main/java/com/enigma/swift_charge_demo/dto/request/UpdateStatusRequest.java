package com.enigma.swift_charge_demo.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateStatusRequest {
    private String orderId;
    private String transactionStatus;
}
