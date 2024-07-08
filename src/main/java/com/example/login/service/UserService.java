package com.example.login.service;


import com.example.login.model.User;
import com.example.login.model.UserDTO;

import java.util.List;
/*
* Den här koden definierar ett servicegränssnitt för användare.
* UserService innehåller metoder för att hitta alla användare, ta bort en användare baserat på e-post,
* registrera en användare från en UserDTO,
* och hitta en användare baserat på e-post.
* */
public interface UserService {
    List<User> findAll();
    void deleteUser(String email);
    User registerUser(UserDTO userDTO);
    User findByEmail(String email);
}
