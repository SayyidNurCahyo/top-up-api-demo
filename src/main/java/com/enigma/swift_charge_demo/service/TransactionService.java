package com.enigma.swift_charge_demo.service;

import com.enigma.swift_charge_demo.dto.request.NewTransactionRequest;
import com.enigma.swift_charge_demo.dto.request.SearchTransactionRequest;
import com.enigma.swift_charge_demo.dto.request.UpdateStatusRequest;
import com.enigma.swift_charge_demo.dto.response.TransactionResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TransactionService {
    TransactionResponse addTransaction(NewTransactionRequest request);
    Page<TransactionResponse> getAllTransaction(SearchTransactionRequest request);
    List<TransactionResponse> getAllByCustomerId(String customerId);
    void updateStatus(UpdateStatusRequest request);
}
