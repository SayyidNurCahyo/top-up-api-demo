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
@Table(name = TableName.PRODUCT)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "product_name", nullable = false, unique = true)
    private String name;
    @Column(name = "product_description", nullable = false)
    private String description;
    @Column(name = "product_price", nullable = false)
    private Long price;
    @Column(name = "availability", nullable = false)
    private Boolean isAvailable;
}
