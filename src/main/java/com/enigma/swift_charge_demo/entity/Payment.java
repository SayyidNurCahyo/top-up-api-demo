package com.enigma.swift_charge_demo.entity;

import com.enigma.swift_charge_demo.constant.TableName;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = TableName.PAYMENT)
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "redirect_url", nullable = false)
    private String redirectUrl;
    @Column(name = "transaction_status", nullable = false)
    private String status;
}
