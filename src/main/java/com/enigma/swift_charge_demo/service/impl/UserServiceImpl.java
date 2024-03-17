package com.enigma.swift_charge_demo.service.impl;

import com.enigma.swift_charge_demo.entity.UserAccount;
import com.enigma.swift_charge_demo.repository.UserAccountRepository;
import com.enigma.swift_charge_demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserAccountRepository userAccountRepository;

    @Transactional(readOnly = true)
    @Override
    public UserAccount loadUserByUsername(String email){
        return userAccountRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Username Not Found"));
    }

    @Transactional(readOnly = true)
    @Override
    public UserAccount getByUserId(String id) {
        return userAccountRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));
    }

    @Transactional(readOnly = true)
    @Override
    public UserAccount getByContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userAccountRepository.findByEmail(authentication.getPrincipal().toString())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found"));
    }
}
