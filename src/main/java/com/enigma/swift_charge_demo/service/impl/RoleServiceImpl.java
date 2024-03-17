package com.enigma.swift_charge_demo.service.impl;

import com.enigma.swift_charge_demo.constant.UserRole;
import com.enigma.swift_charge_demo.entity.Role;
import com.enigma.swift_charge_demo.repository.RoleRepository;
import com.enigma.swift_charge_demo.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Role getRole(UserRole role) {
        return roleRepository.findByRole(role).orElseGet(()-> roleRepository.saveAndFlush(Role.builder().role(role).build()));
    }
}
