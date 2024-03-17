package com.enigma.swift_charge_demo.service;

import com.enigma.swift_charge_demo.entity.Payment;
import com.enigma.swift_charge_demo.entity.Transaction;

public interface PaymentService {
    Payment createPayment(Transaction transaction);
    Payment findById(String id);
}
