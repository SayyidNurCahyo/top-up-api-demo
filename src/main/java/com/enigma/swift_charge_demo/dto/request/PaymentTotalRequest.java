package com.enigma.swift_charge_demo.dto.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentTotalRequest {
    @JsonProperty("order_id")
    private String orderId;
    @JsonProperty("gross_amount")
    private Long amount;
}
