package org.triBhaskar.auth.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.triBhaskar.auth.entity.CoinUser;
import org.triBhaskar.auth.model.*;
import org.triBhaskar.auth.service.UserService;

import java.time.LocalDateTime;

@CrossOrigin("*")
@RequestMapping("/api/v1/user")
@RestController
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest registerRequest) {
        logger.info("Register request received for username: {}", registerRequest.getUsername());
        CoinUser coinUser = userService.registerUser(registerRequest);
        logger.info("User registered successfully: {}", coinUser.getUsername());
        return ResponseEntity.ok(new RegisterResponse("success", "User registered successfully", coinUser.getUsername(), LocalDateTime.now()));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        logger.info("Login request received for identifier: {}", loginRequest.getIdentifier());
        return ResponseEntity.ok(userService.loginUser(loginRequest));
    }

    @PostMapping("/test")
    public ResponseEntity<ApiResponse> testmore(@RequestBody String test) {
        logger.info("Test request received");
        return ResponseEntity.ok(new ApiResponse("success", "Tested successfully"));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        logger.info("Forgot password request received for email: {}", request.getEmail());
        userService.forgotPassword(request.getEmail().trim(), request.getResetPwdUrl());
        logger.info("Forgot password process completed for email: {}", request.getEmail());
        return ResponseEntity.ok(new ApiResponse(
                "success",
                "If the email exists in our system, a password reset link will be sent"
        ));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse> resetPassword(@RequestBody ResetPasswordRequest request) {
        logger.info("Reset password request received for token: {}", request.getToken());
        userService.resetPassword(request.getToken(), request.getNewPassword());
        logger.info("Password reset successfully for token: {}", request.getToken());
        return ResponseEntity.ok(new ApiResponse(
                "success",
                "Password has been reset successfully"
        ));
    }
}