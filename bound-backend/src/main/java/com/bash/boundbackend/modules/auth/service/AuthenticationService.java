package com.bash.boundbackend.modules.auth.service;

import com.bash.boundbackend.common.constants.EmailTemplateName;
import com.bash.boundbackend.modules.auth.dto.request.UserAuthenticationRequest;
import com.bash.boundbackend.modules.auth.dto.request.UserRegistrationRequest;
import com.bash.boundbackend.modules.auth.dto.response.UserAuthenticationResponse;
import com.bash.boundbackend.modules.auth.entity.Token;
import com.bash.boundbackend.modules.auth.entity.User;
import com.bash.boundbackend.modules.auth.repository.RoleRepository;
import com.bash.boundbackend.modules.auth.repository.TokenRepository;
import com.bash.boundbackend.modules.auth.repository.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;

    private static final int CODELENGTH = 6;
    private static final int CODEEXPIRATIONMINUTES = 15;


    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;


//    @Transactional
    public void registerUser(UserRegistrationRequest registrationRequest) throws MessagingException {
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

    private void sendUserRegistrationEmail(User user) throws MessagingException {
        // generate activation token/code and save it to the database
        var newActivationCode = generateAndSaveActivationToken(user);

        // send the user email
        emailService.sendUserEmail(
                user.getEmail(),
                user.getFullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newActivationCode,
                "Account activation"
        );


    }

    private String generateAndSaveActivationToken(User user) {
        tokenRepository.findAllValidTokensByUser(user.getId())
                .forEach(token -> {
                    token.setExpiresAt(LocalDateTime.now());
                    token.setUsed(true);
                });

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

    public UserAuthenticationResponse authenticateUser(UserAuthenticationRequest authenticationRequest) {
        // Authenticate user
        var authenticationObject = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getEmail(),
                    authenticationRequest.getPassword()
            )
        );

        // Get user and create fullName claims
        var userClaims = new HashMap<String,Object>();
        var user = (User) authenticationObject.getPrincipal();
        userClaims.put("fullName", user.getFullName());

        // Generate jwtToken based on the claims and the user
        var jwtToken = jwtTokenService.generateJwtToken(userClaims, user);


        return UserAuthenticationResponse
                .builder()
                .jwtToken(jwtToken)
                .build();
    }

    // atomicity - matters if I'm updating both user and token tables.(same time)
    // Entire operation succeeds ort everything rolls back
//    @Transactional
    public void activateUserAccount(String code) throws MessagingException {
        // TODO - define a more specific exception
        Token savedToken = tokenRepository.findByToken(code)
                .orElseThrow(() -> new RuntimeException("Input correct " +CODELENGTH+"-digit code."));

        if (savedToken.isUsed()) {
            throw new RuntimeException("Activation code already used.");
        }

        // If token is expired send a new one
        if (LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
            sendUserRegistrationEmail(savedToken.getUser());
            throw new RuntimeException("Activation code expired. New token has been sent");

        }

        // Validate the token and save token
        // Mark token as used early (to prevent reuse if concurrent thread sneaks in)
        savedToken.setUsed(true);  //for security completeness
        savedToken.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(savedToken);

        // Get user, enable user account and save the state
        var user = userRepository.findById(savedToken.getUser().getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setEnabled(true);
        //User user = savedToken.getUser();
        userRepository.save(user);



    }
}
