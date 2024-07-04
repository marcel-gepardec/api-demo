package com.example.apitest.infrastructure.jpa;

import com.example.apitest.domain.model.Benutzer;
import com.example.apitest.infrastructure.jpa.entity.BenutzerEntity;
import com.example.apitest.infrastructure.jpa.translator.BenutzerEntityTranslator;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class BenutzerRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private BenutzerEntityTranslator benutzerEntityTranslator;
    @Transactional
    public Benutzer persistBenutzer(final Benutzer benutzer) {
        BenutzerEntity benutzerEntity = benutzerEntityTranslator.fromDomain(benutzer);
        entityManager.persist(benutzerEntity);
        return benutzerEntityTranslator.toDomain(benutzerEntity);
    }

    public Benutzer findByEmail(final String email) {
        final BenutzerEntity benutzerEntity = entityManager.createNamedQuery("BenutzerEntity.findByEmail", BenutzerEntity.class)
                .setParameter("email", email)
                .getSingleResult();

        return benutzerEntityTranslator.toDomain(benutzerEntity);
    }
}
