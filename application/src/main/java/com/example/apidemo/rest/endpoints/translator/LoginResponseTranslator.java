package com.example.apidemo.rest.endpoints.translator;

import com.example.apidemo.domain.model.LoginResponse;
import com.example.apidemo.rest.model.LoginResponseV1;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LoginResponseTranslator {

    public LoginResponseV1 fromDomain(LoginResponse loginResponse) {
        LoginResponseV1 loginResponseV1 = new LoginResponseV1();
        loginResponseV1.setUserPasswordValid(loginResponse.getUserPasswordValid());
        loginResponseV1.setToken(loginResponse.getToken());
        return loginResponseV1;
    }
}
