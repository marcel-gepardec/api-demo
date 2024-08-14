package com.example.apidemo.domain.adapter.out;

import com.example.apidemo.domain.model.Benutzer;

public interface BenutzerRepositoryAdapter {

    public Benutzer persistBenutzer(final Benutzer benutzer);

    public Benutzer findByEmail(final String email);

    public Boolean isEmailExist(final String email);

    public void delete(Benutzer benutzer);
}
