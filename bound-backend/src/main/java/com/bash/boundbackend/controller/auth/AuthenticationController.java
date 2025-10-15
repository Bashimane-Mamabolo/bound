package com.bash.boundbackend.controller.auth;

import com.bash.boundbackend.dto.request.UserRegistrationRequest;
import com.bash.boundbackend.service.AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(
        name = "Authentication controller",
        description = "Handles user reg and login"
)
public class AuthenticationController {

    private final AuthenticationService authenticationService;


    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> registerUser(
        @RequestBody @Valid UserRegistrationRequest registrationRequest
    ) throws MessagingException {
        authenticationService.registerUser(registrationRequest);
        return  ResponseEntity.accepted().build();
    }

}
