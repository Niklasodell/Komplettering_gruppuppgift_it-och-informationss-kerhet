package com.example.login.service;



import com.example.login.model.User;
import com.example.login.model.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
/**
 * Den här koden definierar en tjänst för att ladda användardetaljer.
 * MyUserDetailsService implementerar UserDetailsService och använder UserRepository för att hämta en användare baserat på e-postadress.
 * Om användaren hittas, skapas ett UserDetails-objekt med användarens e-post, lösenord och roll.
 * Rollen konverteras till en samling av GrantedAuthority med getAuthorities-metoden.
 * /@Service markerar klassen som en tjänst i Spring.
 */
@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;


    public MyUserDetailsService(UserRepository userRepository) {
        super();
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("No user found with email: " + username);
        }
        Collection<? extends GrantedAuthority> authorities = getAuthorities(user.getRole());
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), true, true, true, true, authorities);
    }

    private Collection<? extends GrantedAuthority> getAuthorities(String role) {
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }

}
