package com.enigma.swift_charge_demo.entity;

import com.enigma.swift_charge_demo.constant.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = TableName.TRANSACTION)
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "transaction_date", nullable = false, updatable = false)
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date;
    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id",nullable = false)
    private Customer customer;
    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
    private Product product;
    @OneToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;
}
