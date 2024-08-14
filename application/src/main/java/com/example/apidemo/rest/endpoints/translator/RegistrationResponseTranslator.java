package com.example.apidemo.rest.endpoints.translator;

import com.example.apidemo.domain.model.RegistrationResponse;
import com.example.apidemo.rest.model.RegistrationResponseV1;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RegistrationResponseTranslator {

    public RegistrationResponseV1 fromDomain(RegistrationResponse registrationResponse) {
        RegistrationResponseV1 registrationResponseV1 = new RegistrationResponseV1();
        registrationResponseV1.setToken(registrationResponse.getToken());
        return registrationResponseV1;
    }
}
