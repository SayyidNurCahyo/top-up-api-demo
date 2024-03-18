package com.enigma.swift_charge_demo.repository;

import com.enigma.swift_charge_demo.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,String> {
    List<Payment> findAllByStatusIn(List<String> transactionStatus);
}
