package com.enigma.swift_charge_demo.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentRequest {
    @JsonProperty("transaction_details")
    private PaymentTotalRequest paymentDetail;
    @JsonProperty("item_details")
    private PaymentProductRequest paymentItemDetails;
    @JsonProperty("enabled_payments")
    private List<String> paymentMethod;
}
