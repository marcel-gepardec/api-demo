package com.example.apidemo.domain.adapter.in;

import com.example.apidemo.domain.model.LoginData;
import com.example.apidemo.domain.model.LoginResponse;
import com.example.apidemo.domain.model.RegistrationResponse;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.NoResultException;
import jakarta.ws.rs.NotAuthorizedException;

public interface UserControllerUseCase {

    public LoginResponse login(LoginData loginData) throws NotAuthorizedException, NoResultException;

    public RegistrationResponse registration(LoginData loginData) throws NoResultException;

    public Boolean delete(LoginData loginData) throws NotAuthorizedException, NoResultException;
}
