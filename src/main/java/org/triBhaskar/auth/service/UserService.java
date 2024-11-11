package org.triBhaskar.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.triBhaskar.auth.entity.CoinUser;
import org.triBhaskar.auth.exception.EmailAlreadyExistsException;
import org.triBhaskar.auth.exception.UserAlreadyExistsException;
import org.triBhaskar.auth.jwt.Role;
import org.triBhaskar.auth.model.RegisterRequest;
import org.triBhaskar.auth.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


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
}
