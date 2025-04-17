package com.saifeddine.user.service;

import com.saifeddine.user.model.Role;
import com.saifeddine.user.model.User;
import com.saifeddine.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Service
public class SecurityUserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired // Add this annotation
    public SecurityUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // First try to find by email, since that's what the login form sends
        Optional<User> userOptional = userRepository.findByEmail(username);

        // If not found by email, try by username (for backward compatibility)
        if (userOptional.isEmpty()) {
            userOptional = userRepository.findByUserName(username);
        }

        User user = userOptional.orElseThrow(() ->
                new UsernameNotFoundException("User not found with email/username: " + username));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),  // Use email as the principal
                user.getPassword(),
                getAuthorities(user.getRole())
        );
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Role role) {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }
}
