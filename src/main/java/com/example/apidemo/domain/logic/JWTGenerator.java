package com.example.apidemo.domain.logic;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@ApplicationScoped
public class JWTGenerator {

    private Algorithm algorithmPrivate;
    private Algorithm algorithmPublic;

    @PostConstruct
    public void createAlgorithm() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048);
            KeyPair pair = generator.generateKeyPair();

            RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) pair.getPrivate();
            algorithmPrivate = Algorithm.RSA512(rsaPrivateKey);

            RSAPublicKey rsaPublicKey = (RSAPublicKey) pair.getPublic();
            algorithmPublic = Algorithm.RSA512(rsaPublicKey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String generate(Long userId) {
        return generate(userId, Instant.now().plus(5, ChronoUnit.MINUTES));
    }

    public String generate(Long userId, Instant expirationTime) {
        return generate(userId, Instant.now(), expirationTime);
    }

    public String generate(Long userId, Instant notBeforeTime, Instant expirationTime) {
        try {
            return JWT.create()
                    .withIssuer("API Demo")
                    .withSubject("API Demo")
                    .withJWTId(UUID.randomUUID().toString())
                    .withClaim("userId", userId)
                    .withIssuedAt(Instant.now())
                    .withExpiresAt(expirationTime)
                    .withNotBefore(notBeforeTime)
                    .sign(algorithmPrivate);
        } catch (JWTCreationException exception) {
            // Invalid Signing configuration / Couldn't convert Claims.
        }
        return null;
    }

    public void validateToken(String token) throws Exception {
        JWTVerifier verifier = JWT.require(algorithmPublic)
                // specify an specific claim validations
                .withIssuer("API Demo")
                .withSubject("API Demo")
                // reusable verifier instance
                .build();

        verifier.verify(token);
    }

    public boolean isTokenValid(String token) {
        try {
            JWTVerifier verifier = JWT.require(algorithmPublic)
                    .withIssuer("test")
                    .build();

            verifier.verify(token);
            return true;

        } catch (Exception e) {
            return false;
        }
    }
}
