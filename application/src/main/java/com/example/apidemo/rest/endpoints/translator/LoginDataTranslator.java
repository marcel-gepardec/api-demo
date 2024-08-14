package com.example.apidemo.rest.endpoints.translator;

import com.example.apidemo.domain.model.LoginData;
import com.example.apidemo.rest.model.LoginDataV1;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LoginDataTranslator {

    public LoginData toDomain(LoginDataV1 loginDataV1) {
        LoginData loginData = new LoginData();
        loginData.setEmail(loginDataV1.getEmail());
        loginData.setPassword(loginDataV1.getPassword());
        return loginData;
    }
}
