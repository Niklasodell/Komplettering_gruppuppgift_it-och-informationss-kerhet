package com.example.login.service;


import com.example.login.model.User;
import com.example.login.model.UserDTO;
import com.example.login.model.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * Denna kod definierar klassen UserServiceImpl som hanterar användaroperationer genom att implementera UserService-gränssnittet.
 * Klassen använder UserRepository för databasåtgärder och PasswordEncoder för att kryptera lösenord.
 * De huvudsakliga funktionerna inkluderar att hitta alla användare, ta bort en användare via e-post,
 * registrera en ny användare med krypterat lösenord och hitta en användare baserat på e-post.
 *
 * Klassen är markerad som en servicekomponent i Spring med @Service.
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            userRepository.delete(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    @Override
    public User registerUser(UserDTO userDTO) {
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRole("ROLE_USER");
        User savedUser = userRepository.save(user);
        logger.debug( "User saved with ID: {}", savedUser.getId() );
        return savedUser;
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}
