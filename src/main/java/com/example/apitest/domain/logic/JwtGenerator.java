package com.example.apitest.domain.logic;

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

@ApplicationScoped
public class JwtGenerator {

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

    public String generate() {
        try {
            return JWT.create()
                    .withIssuer("ApiTest")
                    .withSubject("TestToken")
                    .withClaim("email", "testtoken@gmail.com")
                    .withExpiresAt(Instant.now().plus(30, ChronoUnit.MINUTES))
                    .sign(algorithmPrivate);
        } catch (JWTCreationException exception) {
            // Invalid Signing configuration / Couldn't convert Claims.
        }
        return null;
    }

    public String generate(String email) {
        try {
            return JWT.create()
                    .withIssuer("test")
                    .withSubject("123456789")
                    .withClaim("email", email)
                    .withExpiresAt(Instant.now().plus(30, ChronoUnit.MINUTES))
                    .sign(algorithmPrivate);
        } catch (JWTCreationException exception) {
            // Invalid Signing configuration / Couldn't convert Claims.
        }
        return null;
    }

    public String generate(String email, Instant expirationTime) {
        try {
            return JWT.create()
                    .withIssuer("test")
                    .withSubject("123456789")
                    .withClaim("email", email)
                    .withExpiresAt(expirationTime)
                    .sign(algorithmPrivate);
        } catch (JWTCreationException exception) {
            // Invalid Signing configuration / Couldn't convert Claims.
        }
        return null;
    }

    public void validateToken(String token) throws Exception {
        JWTVerifier verifier = JWT.require(algorithmPublic)
                // specify an specific claim validations
                .withIssuer("test")
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
