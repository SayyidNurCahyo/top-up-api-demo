package com.enigma.swift_charge_demo.entity;


import com.enigma.swift_charge_demo.constant.TableName;
import com.enigma.swift_charge_demo.constant.UserRole;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = TableName.ROLE)
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserRole role;
}
