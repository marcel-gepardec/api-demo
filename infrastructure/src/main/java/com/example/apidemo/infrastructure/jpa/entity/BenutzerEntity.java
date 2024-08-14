package com.example.apidemo.infrastructure.jpa.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "BENUTZER")
@NamedQuery(name = "BenutzerEntity.findByEmail", query = "select b from BenutzerEntity b where b.email = :email")
public class BenutzerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Basic
    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;

    @Basic
    @Column(name = "PASSWORD", nullable = false)
    private String password;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
