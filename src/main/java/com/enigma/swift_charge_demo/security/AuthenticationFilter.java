package com.enigma.swift_charge_demo.security;

import com.enigma.swift_charge_demo.dto.response.JwtResponse;
import com.enigma.swift_charge_demo.entity.UserAccount;
import com.enigma.swift_charge_demo.repository.UserAccountRepository;
import com.enigma.swift_charge_demo.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserAccountRepository userAccountRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token =request.getHeader("Authorization");
            if (token != null && jwtService.verifyJwtToken(token)) {
                JwtResponse jwtResponse =jwtService.getClaimsByToken(token);
                UserAccount account = userAccountRepository.findById(jwtResponse.getUserAccountId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user account not found"));
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        account.getUsername(), null, account.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (Exception e){
            throw new RuntimeException();
        }
        filterChain.doFilter(request,response);
    }
}
