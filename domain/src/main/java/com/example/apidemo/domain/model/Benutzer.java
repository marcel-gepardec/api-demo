package com.example.apidemo.domain.model;

import java.util.Objects;

public class Benutzer {

    private Long id;

    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Benutzer benutzer = (Benutzer) o;
        return Objects.equals(id, benutzer.id) && Objects.equals(email, benutzer.email) && Objects.equals(password, benutzer.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password);
    }

    @Override
    public String toString() {
        return "Benutzer{" +
               "id=" + id +
               ", email='" + email + '\'' +
               ", password='" + password + '\'' +
               '}';
    }
}
