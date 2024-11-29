package org.triBhaskar.auth.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.triBhaskar.auth.entity.CoinUser;
import org.triBhaskar.auth.exception.*;
import org.triBhaskar.auth.jwt.JwtTokenProvider;
import org.triBhaskar.auth.jwt.Role;
import org.triBhaskar.auth.model.*;
import org.triBhaskar.auth.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private TokenService tokenService;

    public CoinUser registerUser(RegisterRequest registerRequest) {
        logger.info("Registering user with username: {}", registerRequest.getUsername());
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            logger.warn("Username already exists: {}", registerRequest.getUsername());
            throw new UserAlreadyExistsException("Username already exists");
        }
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            logger.warn("Email already exists: {}", registerRequest.getEmail());
            throw new EmailAlreadyExistsException("Email already exists");
        }
        CoinUser user = new CoinUser();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setRole(Role.ROLE_USER);

        CoinUser savedUser = userRepository.save(user);
        logger.info("User registered successfully with username: {}", savedUser.getUsername());
        return savedUser;
    }

    public LoginResponse loginUser(LoginRequest loginRequest) {
        logger.info("Logging in user with identifier: {}", loginRequest.getIdentifier());
        Optional<CoinUser> user;

        if (loginRequest.getIdentifier() != null && !loginRequest.getIdentifier().isEmpty()) {
            user = userRepository.findByEmail(loginRequest.getIdentifier());
            if (user.isEmpty()) {
                user = userRepository.findByUsername(loginRequest.getIdentifier());
                if (user.isEmpty()) {
                    logger.warn("User not found with identifier: {}", loginRequest.getIdentifier());
                    throw new UserNotFoundException("User does not exist");
                }
            }
        } else {
            logger.warn("Identifier must be provided");
            throw new InvalidCredentialsException("Identifier must be provided");
        }

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.get().getPassword())) {
            logger.warn("Password is incorrect for user: {}", user.get().getUsername());
            throw new InvalidCredentialsException("Password is incorrect");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.get().getUsername(), loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        LoginResponse response = new LoginResponse("success", "User logged in successfully", user.get().getUsername(), tokenProvider.generateToken(authentication), LocalDateTime.now());
        logger.info("User logged in successfully with username: {}", user.get().getUsername());
        return response;
    }

    public void forgotPassword(String email, String resetPwdUrl) {
        logger.info("Processing forgot password for email: {}", email);
        Optional<CoinUser> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            if (tokenService.isRateLimited(email)) {
                logger.warn("Too many password reset attempts for email: {}", email);
                throw new ToMannyAttemptsException("Too many password reset attempts. Please try again later.");
            }

            String token = UUID.randomUUID().toString();
            tokenService.saveToken(email, token);

            try {
                emailService.sendPasswordResetEmail(user.get().getEmail(), token, resetPwdUrl);
                logger.info("Password reset email sent successfully to: {}", email);
            } catch (Exception e) {
                tokenService.deleteToken(token);
                logger.error("Failed to send password reset email to: {}", email, e);
                throw new FailedToSendEmailException("Failed to send password reset email");
            }
        } else {
            logger.warn("User not found with email: {}", email);
            throw new UserNotFoundException("User does not exist with the provided email");
        }
    }

    public void resetPassword(String token, String newPassword) {
        logger.info("Resetting password for token: {}", token);
        String email = tokenService.getEmailFromToken(token);

        if (email == null) {
            logger.warn("Invalid or expired password reset link for token: {}", token);
            throw new PasswordResetLinkExpiredException("Invalid or expired password reset link");
        }

        Optional<CoinUser> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            logger.warn("User not found for email: {}", email);
            throw new UserNotFoundException("User not found");
        }

        user.get().setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user.get());
        tokenService.deleteToken(token);
        logger.info("Password reset successfully for email: {}", email);
    }
}