package com.enigma.swift_charge_demo.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.enigma.swift_charge_demo.dto.response.JwtResponse;
import com.enigma.swift_charge_demo.entity.UserAccount;
import com.enigma.swift_charge_demo.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@Service
public class JwtServiceImpl implements JwtService {
    @Value("${scharge.jwt.secret_key}")
    private String jwtSecret;
    @Value("${scharge.jwt.issuer}")
    private String issuer;
    @Value("${scharge.jwt.expirationInSecond}")
    private Long jwtExpiration;

    @Override
    public String generateToken(UserAccount account) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(jwtSecret);
            return JWT.create().withSubject(account.getId())
                    .withClaim("roles", account.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                    .withIssuedAt(Instant.now()).withExpiresAt(Instant.now().plusSeconds(jwtExpiration))
                    .withIssuer(issuer).sign(algorithm);
        } catch (JWTCreationException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "error while generate token");
        }
    }

    @Override
    public Boolean verifyJwtToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(jwtSecret);
            JWTVerifier jwtVerifier = JWT.require(algorithm).withIssuer(issuer).build();
            jwtVerifier.verify(parseJwt(token));
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    @Override
    public JwtResponse getClaimsByToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(jwtSecret);
            JWTVerifier jwtVerifier = JWT.require(algorithm).withIssuer(issuer).build();
            DecodedJWT decodedJWT = jwtVerifier.verify(parseJwt(token));
            return JwtResponse.builder().userAccountId(decodedJWT.getSubject())
                    .roles(decodedJWT.getClaim("roles").asList(String.class)).build();
        } catch (JWTVerificationException e) {
            return null;
        }
    }

    private String parseJwt(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }
}
