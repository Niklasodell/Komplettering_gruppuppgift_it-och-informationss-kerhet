package com.example.login.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/*
* Den här koden definierar en DTO-klass för användare.
* Klassen UserDTO innehåller fält för e-post och lösenord med valideringsregler:
* e-post måste vara en giltig och icke-blank, lösenord måste vara minst 6 tecken och icke-blank.
*
* @Getter och @Setter genererar getter- och setter-metoder.
* */

@Setter
@Getter
public class UserDTO implements Serializable {

    @NotBlank(message = "Email is required")
  @Email(message = "Please provide a valid email")
  private String email;

  @NotBlank(message = "Password is required")
  @Size(min = 6, message = "Password must be at least 6 characters")
  private String password;

}
