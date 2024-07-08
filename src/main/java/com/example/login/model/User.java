package com.example.login.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/*
* Den här koden definierar en JPA-entitet för användare i en databas.
* Klassen User har fält för ID, e-post, lösenord och roll.
* Annoteringar används för att mappa dessa fält till en tabell som heter "users".
* @Id markerar primärnyckeln, som genereras automatiskt. E-post är unik och får inte vara null,
* och både lösenord och roll får inte vara null.
*
* @Getter och @Setter genererar getter- och setter-metoder för alla fält.
* */

@Setter
@Getter
@Entity
@Table(name = "users")
public class User implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;

}

