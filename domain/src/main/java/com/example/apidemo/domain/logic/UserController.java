package com.example.apidemo.domain.logic;

import com.example.apidemo.domain.adapter.in.UserControllerUseCase;
import com.example.apidemo.domain.adapter.out.BenutzerRepositoryAdapter;
import com.example.apidemo.domain.model.Benutzer;
import com.example.apidemo.domain.model.LoginData;
import com.example.apidemo.domain.model.LoginResponse;
import com.example.apidemo.domain.model.RegistrationResponse;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.NoResultException;
import jakarta.ws.rs.NotAuthorizedException;

@RequestScoped
public class UserController implements UserControllerUseCase {

    @Inject
    BenutzerRepositoryAdapter benutzerRepository;

    @Inject
    PasswordEncoder passwordEncoder;

    @Inject
    JWTGenerator jwtGenerator;

    @Override
    public LoginResponse login(LoginData loginData) throws NotAuthorizedException, NoResultException {
        String token = null;

        Benutzer benutzer = benutzerRepository.findByEmail(loginData.getEmail());
        Boolean passwordMatches = passwordEncoder.matchingPasswords(loginData.getPassword(), benutzer.getPassword());

        if (Boolean.TRUE.equals(passwordMatches)) {
            token = jwtGenerator.generate(benutzer.getId());
        } else {
            throw new NotAuthorizedException("Not authorized");
        }

        LoginResponse loginResult = new LoginResponse();
        loginResult.setUserPasswordValid(passwordMatches);
        loginResult.setToken(token);

        return loginResult;
    }

    @Override
    public RegistrationResponse registration(LoginData loginData) throws NoResultException {
        if (benutzerRepository.isEmailExist(loginData.getEmail())) {
            throw new NoResultException();
        }

        Benutzer benutzer = new Benutzer();
        benutzer.setEmail(loginData.getEmail());
        benutzer.setPassword(passwordEncoder.encodePasswordWithArgon2(loginData.getPassword()));

        benutzer = benutzerRepository.persistBenutzer(benutzer);

        RegistrationResponse registrationResponse = new RegistrationResponse();
        registrationResponse.setToken(jwtGenerator.generate(benutzer.getId()));
        return registrationResponse;
    }

    @Override
    public Boolean delete(LoginData loginData) throws NotAuthorizedException, NoResultException  {
        Benutzer benutzer = benutzerRepository.findByEmail(loginData.getEmail());
        Boolean passwordMatches = passwordEncoder.matchingPasswords(loginData.getPassword(), benutzer.getPassword());

        if (Boolean.TRUE.equals(passwordMatches)) {
            benutzerRepository.delete(benutzer);
            return true;
        } else {
            throw new NotAuthorizedException("Not authorized");
        }
    }
}
