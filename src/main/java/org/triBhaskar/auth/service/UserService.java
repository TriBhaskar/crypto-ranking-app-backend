package org.triBhaskar.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.triBhaskar.auth.entity.CoinUser;
import org.triBhaskar.auth.exception.EmailAlreadyExistsException;
import org.triBhaskar.auth.exception.InvalidCredentialsException;
import org.triBhaskar.auth.exception.UserAlreadyExistsException;
import org.triBhaskar.auth.exception.UserNotFoundException;
import org.triBhaskar.auth.jwt.JwtTokenProvider;
import org.triBhaskar.auth.jwt.Role;
import org.triBhaskar.auth.model.LoginRequest;
import org.triBhaskar.auth.model.LoginResponse;
import org.triBhaskar.auth.model.RegisterRequest;
import org.triBhaskar.auth.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public CoinUser registerUser(RegisterRequest registerRequest) {
        // check if user exists
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists");
        }
        // check if email exists
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }
        CoinUser user = new CoinUser();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setRole(Role.ROLE_USER); // Set default role

        return userRepository.save(user);
    }

    public LoginResponse loginUser(LoginRequest loginRequest) {
        Optional<CoinUser> user;

        if (loginRequest.getIdentifier() != null && !loginRequest.getIdentifier().isEmpty()) {
            user = userRepository.findByEmail(loginRequest.getIdentifier());
            if (user.isEmpty()) {
                user = userRepository.findByUsername(loginRequest.getIdentifier());
                if (user.isEmpty()) {
                    throw new UserNotFoundException("User does not exist");
                }
            }
        } else {
            throw new InvalidCredentialsException("Identifier must be provided");
        }

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.get().getPassword())) {
            throw new InvalidCredentialsException("Password is incorrect");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.get().getUsername(), loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new LoginResponse("success", "User logged in successfully", user.get().getUsername(), tokenProvider.generateToken(authentication), LocalDateTime.now());
    }
}
