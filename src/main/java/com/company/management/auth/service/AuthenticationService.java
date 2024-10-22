package com.company.management.auth.service;

import com.company.management.auth.dto.request.LoginUserRequest;
import com.company.management.auth.dto.request.RegisterUserRequest;
import com.company.management.auth.dto.response.LoginDTO;
import com.company.management.auth.dto.response.UserDTO;
import com.company.management.auth.model.User;
import com.company.management.auth.repository.UserRepository;
import com.company.management.configuration.exception.AlreadyExistException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public UserDTO signup(RegisterUserRequest request) {
        validateEmailIsAvailable(request.getEmail());

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User savedUser = userRepository.save(user);

        UserDTO userDTO = new UserDTO();
        userDTO.setName(savedUser.getName());
        userDTO.setEmail(savedUser.getEmail());
        return userDTO;
    }

    public LoginDTO authenticate(LoginUserRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User authenticatedUser =  userRepository.findByEmail(request.getEmail())
                .orElseThrow();

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginDTO login = new LoginDTO();
        login.setToken(jwtToken);
        login.setExpiresIn(jwtService.getExpirationTime());

        return login;
    }

    private void validateEmailIsAvailable(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new AlreadyExistException("email.already.taken");
        }
    }
}