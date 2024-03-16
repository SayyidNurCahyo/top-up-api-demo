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
@Table(name = TableName.CUSTOMER)
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "customer_name", nullable = false)
    private String name;
    @Column(name = "customer_phone", nullable = false)
    private String phone;
    @Column(name = "customer_email", nullable = false)
    private String email;
    @OneToOne
    @Column(name = "user_account_id")
    private UserAccount userAccount;
}
