package com.example.apidemo.domain.model;

import java.util.Objects;

public class LoginResponse {

    private Boolean userPasswordValid;

    private String token;

    public Boolean getUserPasswordValid() {
        return userPasswordValid;
    }

    public void setUserPasswordValid(Boolean userPasswordValid) {
        this.userPasswordValid = userPasswordValid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginResponse that = (LoginResponse) o;
        return Objects.equals(userPasswordValid, that.userPasswordValid) && Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userPasswordValid, token);
    }
}
