package com.enigma.swift_charge_demo.service.impl;

import com.enigma.swift_charge_demo.dto.request.PaymentProductRequest;
import com.enigma.swift_charge_demo.dto.request.PaymentRequest;
import com.enigma.swift_charge_demo.dto.request.PaymentTotalRequest;
import com.enigma.swift_charge_demo.entity.Payment;
import com.enigma.swift_charge_demo.entity.Transaction;
import com.enigma.swift_charge_demo.repository.PaymentRepository;
import com.enigma.swift_charge_demo.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final RestClient restClient;
    @Value("${midtrans.api.key}")
    private String secretKey;
    @Value("${midtrans.api.snap-url}")
    private String baseUrl;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public Payment createPayment(Transaction transaction) {
        PaymentProductRequest productRequest = PaymentProductRequest.builder()
                .name(transaction.getProduct().getName()).price(transaction.getProduct().getPrice()).build();
        PaymentRequest request = PaymentRequest.builder()
                .paymentDetail(PaymentTotalRequest.builder()
                        .orderId(UUID.randomUUID().toString())
                        .amount(transaction.getProduct().getPrice()).build())
                .paymentItemDetails(productRequest)
                .paymentMethod(List.of("gopay", "indomaret", "shopeepay")).build();
        ResponseEntity<Map<String, String>> response = restClient.post()
                .uri(baseUrl).body(request)
                .header(HttpHeaders.AUTHORIZATION, "Basic " + secretKey)
                .retrieve().toEntity(new ParameterizedTypeReference<Map<String, String>>() {});
        Map<String, String> body = response.getBody();
        Payment payment = Payment.builder().id(request.getPaymentDetail().getOrderId())
                .redirectUrl(body.get("redirect_url"))
                .status("ordered").build();
        return paymentRepository.saveAndFlush(payment);
    }

    @Transactional(readOnly = true)
    @Override
    public Payment findById(String id) {
        return paymentRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "payment not found"));
    }
}
