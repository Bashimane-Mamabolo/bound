package com.bash.boundbackend.service;

import com.bash.boundbackend.dto.request.UserRegistrationRequest;
import com.bash.boundbackend.entity.user.User;
import com.bash.boundbackend.repository.RoleRepository;
import com.bash.boundbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public void registerUser(UserRegistrationRequest registrationRequest) {
        // TODO - better fail mechanism
        
        // assign user role by default
        var userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new IllegalArgumentException("ROLE USER not initialized"));
        // build user object and persist it to the database
        var user = User.builder()
                .firstname(registrationRequest.getFirstname())
                .lastname(registrationRequest.getLastname())
                .email(registrationRequest.getEmail())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .accountLocked(false)
                .enabled(false)
                .roles(List.of(userRole))
                .build();

        userRepository.save(user);
        // send the user validation email
        sendUserRegistrationEmail(user);
    }

    private void sendUserRegistrationEmail(User user) {
        //generate jwt activation token and save it to the database

        // send the user email
    }
}
