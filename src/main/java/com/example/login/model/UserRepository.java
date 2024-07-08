package com.example.login.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Den här koden definierar ett repository för användare.
 * UserRepository är ett gränssnitt som utökar JpaRepository för att hantera User-entiteter med Long som ID-typ.
 * Det innehåller en metod findByEmail för att hitta en användare baserat på e-postadress.
 * /@Repository markerar gränssnittet som ett Spring-repository.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
