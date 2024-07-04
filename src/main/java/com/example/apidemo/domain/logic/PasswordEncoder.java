package com.example.apidemo.domain.logic;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

@ApplicationScoped
public class PasswordEncoder {

    private Argon2PasswordEncoder argon2PasswordEncoder;

    @PostConstruct
    public void createArgon2PasswordEncoder(){
        argon2PasswordEncoder = new Argon2PasswordEncoder(16, 32, 1, 60000, 10);
    }

    public String encodePasswordWithArgon2(String rawPassword) {
        return argon2PasswordEncoder.encode(rawPassword);
    }

    public Boolean matchingPasswords(String rawPassword, String encodedPassword) {
        return argon2PasswordEncoder.matches(rawPassword, encodedPassword);
    }
}
