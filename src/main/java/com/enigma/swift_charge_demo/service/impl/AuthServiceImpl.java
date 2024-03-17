package com.enigma.swift_charge_demo.service.impl;

import com.enigma.swift_charge_demo.constant.UserRole;
import com.enigma.swift_charge_demo.dto.request.LoginRequest;
import com.enigma.swift_charge_demo.dto.request.RegisterRequest;
import com.enigma.swift_charge_demo.dto.response.LoginResponse;
import com.enigma.swift_charge_demo.dto.response.RegisterResponse;
import com.enigma.swift_charge_demo.entity.Customer;
import com.enigma.swift_charge_demo.entity.Role;
import com.enigma.swift_charge_demo.entity.UserAccount;
import com.enigma.swift_charge_demo.repository.UserAccountRepository;
import com.enigma.swift_charge_demo.service.AuthService;
import com.enigma.swift_charge_demo.service.CustomerService;
import com.enigma.swift_charge_demo.service.JwtService;
import com.enigma.swift_charge_demo.service.RoleService;
import com.enigma.swift_charge_demo.util.ValidationUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RoleService roleService;
    private final CustomerService customerService;
    private final JwtService jwtService;
    private final ValidationUtil validationUtil;
    @Value("${scharge.email.admin}")
    private String emailAdmin;
    @Value("${scharge.password.admin}")
    private String passwordAdmin;

    @Transactional(rollbackFor = Exception.class)
    @PostConstruct
    public void initAdmin(){
        Optional<UserAccount> account = userAccountRepository.findByEmail(emailAdmin);
        if (account.isEmpty()) {
            Role role = roleService.getRole(UserRole.ROLE_ADMIN);
            UserAccount userAccount = UserAccount.builder().email(emailAdmin)
                    .roles(List.of(role)).password(passwordEncoder.encode(passwordAdmin))
                    .isEnabled(true).build();
            userAccountRepository.saveAndFlush(userAccount);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RegisterResponse register(RegisterRequest request) {
        validationUtil.validate(request);
        Role role = roleService.getRole(UserRole.ROLE_CUSTOMER);
        String password = passwordEncoder.encode(request.getPassword());
        UserAccount userAccount = UserAccount.builder().email(request.getEmail())
                .roles(List.of(role)).password(password)
                .isEnabled(true).build();
        userAccountRepository.saveAndFlush(userAccount);
        Customer customer = Customer.builder().name(request.getName())
                .phone(request.getPhone()).userAccount(userAccount).build();
        customerService.create(customer);
        return RegisterResponse.builder().email(request.getEmail())
                .roles(List.of(role.getRole().name())).build();
    }

    @Transactional(readOnly = true)
    @Override
    public LoginResponse login(LoginRequest request) {
        validationUtil.validate(request);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        );
        Authentication authenticate = authenticationManager.authenticate(authentication);
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        UserAccount userAccount = (UserAccount) authenticate.getPrincipal();
        String token = jwtService.generateToken(userAccount);
        return LoginResponse.builder()
                .email(userAccount.getEmail())
                .roles(userAccount.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .token(token)
                .build();
    }
}
