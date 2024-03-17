package com.enigma.swift_charge_demo.service;

import com.enigma.swift_charge_demo.dto.response.TransactionResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TransactionService {
    TransactionResponse addTransaction(NewTransactionRequest request);
    Page<GetTransactionResponse> getAllTransaction(SearchTransactionRequest request);
    List<GetTransactionResponse> getAllByCustomerId(String customerId);
    void updateStatus(UpdateStatusRequest request);
    List<GetTransactionResponse> getTransaction();
}
