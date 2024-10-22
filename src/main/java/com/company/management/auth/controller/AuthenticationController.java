package com.company.management.auth.controller;

import com.company.management.auth.dto.request.LoginUserRequest;
import com.company.management.auth.dto.request.RegisterUserRequest;
import com.company.management.auth.dto.response.LoginDTO;
import com.company.management.auth.dto.response.UserDTO;
import com.company.management.auth.model.User;
import com.company.management.auth.service.AuthenticationService;
import com.company.management.auth.service.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signup(@RequestBody @Valid RegisterUserRequest request) {
        UserDTO registeredUser = authenticationService.signup(request);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginDTO> authenticate(@RequestBody @Valid LoginUserRequest request) {
        LoginDTO login = authenticationService.authenticate(request);
        return ResponseEntity.ok(login);
    }
}