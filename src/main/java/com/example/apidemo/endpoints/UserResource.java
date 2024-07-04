package com.example.apidemo.endpoints;

import com.example.apidemo.domain.logic.JwtGenerator;
import com.example.apidemo.domain.logic.PasswordEncoder;
import com.example.apidemo.domain.model.Benutzer;
import com.example.apidemo.infrastructure.jpa.BenutzerRepository;
import com.example.apidemo.rest.model.LoginResponseV1;
import com.example.apidemo.rest.model.LoginV1;
import com.example.apidemo.rest.model.RegistrationResponseV1;
import jakarta.inject.Inject;
import jakarta.persistence.NoResultException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;

@Path("/user")
public class UserResource {

    @Inject
    private Logger logger;

    @Inject
    private JwtGenerator jwtGenerator;

    @Inject
    private BenutzerRepository benutzerRepository;

    @Inject
    private PasswordEncoder passwordEncoder;

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginV1 loginData) {
        Boolean passwordMatches = false;
        String token = null;

        LoginResponseV1 loginResultV1;
        try {
            Benutzer benutzer = benutzerRepository.findByEmail(loginData.getEmail());
            passwordMatches = passwordEncoder.matchingPasswords(loginData.getPassword(), benutzer.getPassword());
            if (Boolean.TRUE.equals(passwordMatches)) {
                token = jwtGenerator.generate(loginData.getEmail());
            }

        } catch (NoResultException ignored) {
        } finally {
            loginResultV1 = new LoginResponseV1();
            loginResultV1.setUserPasswordValid(passwordMatches);
            loginResultV1.setToken(token);
        }
        return Response.status(200).entity(loginResultV1).build();
    }


    @POST
    @Path("/registration")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registration(LoginV1 loginData) {
        Benutzer benutzer = new Benutzer();

        if (!loginData.getEmail().contains("@")) {
            return Response.status(400).entity("It's not a valid email address.").build();
        }

        try {
            benutzerRepository.findByEmail(loginData.getEmail());

        } catch (NoResultException ignored) {
            benutzer.setEmail(loginData.getEmail());
            benutzer.setPassword(passwordEncoder.encodePasswordWithArgon2(loginData.getPassword()));

            benutzerRepository.persistBenutzer(benutzer);
        }

        RegistrationResponseV1 registrationResultV1 = new RegistrationResponseV1();
        registrationResultV1.setToken(jwtGenerator.generate(loginData.getEmail()));
        return Response.status(200).entity(registrationResultV1).build();
    }
}