package com.enigma.swift_charge_demo.service;

import com.enigma.swift_charge_demo.constant.UserRole;
import com.enigma.swift_charge_demo.entity.Role;

public interface RoleService {
    Role getRole(UserRole role);
}
