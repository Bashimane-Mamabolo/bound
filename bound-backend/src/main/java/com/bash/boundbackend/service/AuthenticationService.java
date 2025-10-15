package com.bash.boundbackend.service;

import com.bash.boundbackend.dto.request.UserRegistrationRequest;
import com.bash.boundbackend.entity.user.Token;
import com.bash.boundbackend.entity.user.User;
import com.bash.boundbackend.repository.RoleRepository;
import com.bash.boundbackend.repository.TokenRepository;
import com.bash.boundbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private static final int CODELENGTH = 6;
    private static final int CODEEXPIRATIONMINUTES = 15;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

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
        // generate activation token/code and save it to the database
        var newActivationCode = generateAndSaveActivationToken(user);

        // send the user email
        

    }

    private String generateAndSaveActivationToken(User user) {
        // Generate CODE, build Token/code and save it to DB
        String generatedCode = generateActivationCode();
        var token = Token.builder()
                .token(generatedCode)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(CODEEXPIRATIONMINUTES))
                .user(user)
                .build();
        tokenRepository.save(token);
        return generatedCode;
    }

    private String generateActivationCode() {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < CODELENGTH; i++) {
            int randomIndex =  secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }
        return codeBuilder.toString();
    }
}
