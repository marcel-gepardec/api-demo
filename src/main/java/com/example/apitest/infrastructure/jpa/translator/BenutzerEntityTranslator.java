package com.example.apitest.infrastructure.jpa.translator;

import com.example.apitest.domain.model.Benutzer;
import com.example.apitest.infrastructure.jpa.entity.BenutzerEntity;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BenutzerEntityTranslator {

    public Benutzer toDomain(BenutzerEntity benutzerEntity){
        Benutzer benutzer = new Benutzer();
        benutzer.setId(benutzerEntity.getId());
        benutzer.setEmail(benutzerEntity.getEmail());
        benutzer.setPassword(benutzerEntity.getPassword());
        return benutzer;
    }

    public BenutzerEntity fromDomain(Benutzer benutzer) {
        BenutzerEntity benutzerEntity = new BenutzerEntity();
        benutzerEntity.setId(benutzer.getId());
        benutzerEntity.setEmail(benutzer.getEmail());
        benutzerEntity.setPassword(benutzer.getPassword());
        return benutzerEntity;
    }
}
