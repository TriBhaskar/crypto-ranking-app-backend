package com.tribhaskar.auth.controller;

import com.tribhaskar.auth.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.tribhaskar.auth.entity.CoinUser;
import com.tribhaskar.auth.service.UserService;
import com.tribhaskar.utils.Utility;

import java.time.LocalDateTime;
@CrossOrigin("*")
@RequestMapping("/api/v1/user")
@RestController
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    /**
     * Registers a new user.
     *
     * @param registerRequest the registration request containing user details
     * @return a response entity containing the registration response
     */
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest registerRequest) {
        logger.info("Register request received for username: {}", registerRequest.getUsername());
        CoinUser coinUser = userService.registerUser(registerRequest);
        logger.info("User registered successfully: {}", coinUser.getUsername());
        return ResponseEntity.ok(new RegisterResponse("success", "User registered successfully, " +
                "Please Verify your email ["+ Utility.maskEmail(registerRequest.getEmail()) + "] with OTP sent",
                coinUser.getUsername(), LocalDateTime.now()));
    }

    /**
     * Verifies the user's email.
     *
     * @param request the email verification request containing email and OTP
     * @return a response entity containing the API response
     */
    @PostMapping("/verify-email")
    public ResponseEntity<ApiResponse> verifyEmail(@RequestBody VerifyEmailRequest request) {
        logger.info("Verify email request received for email: {}", request.getEmail());
        userService.verifyEmail(request.getEmail(), request.getOtp());
        logger.info("Email verified successfully for email: {}", request.getEmail());
        return ResponseEntity.ok(new ApiResponse(
                "success",
                "Email verified successfully"
        ));
    }

    /**
     * Resends the OTP to the user's email.
     *
     * @param request the resend OTP request containing the user's email
     * @return a response entity containing the API response
     */
    @PostMapping("/resend-otp")
    public ResponseEntity<ApiResponse> resendOtp(@RequestBody ResendOtpRequest request) {
        logger.info("Resend OTP request received for email: {}", request.getEmail());
        userService.resendOtp(request);
        return ResponseEntity.ok(new ApiResponse(
                "success",
                "OTP resent successfully"
        ));
    }

    /**
     * Logs in a user.
     *
     * @param loginRequest the login request containing user credentials
     * @return a response entity containing the login response
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        logger.info("Login request received for identifier: {}", loginRequest.getIdentifier());
        return ResponseEntity.ok(userService.loginUser(loginRequest));
    }

    /**
     * Test endpoint.
     *
     * @param test the test string
     * @return a response entity containing the API response
     */
    @PostMapping("/test")
    public ResponseEntity<ApiResponse> testmore(@RequestBody String test) {
        logger.info("Test request received");
        return ResponseEntity.ok(new ApiResponse("success", "Tested successfully"));
    }

    /**
     * Initiates the forgot password process.
     *
     * @param request the forgot password request containing email and reset URL
     * @return a response entity containing the API response
     */
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

    /**
     * Resets the user's password.
     *
     * @param request the reset password request containing token and new password
     * @return a response entity containing the API response
     */
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