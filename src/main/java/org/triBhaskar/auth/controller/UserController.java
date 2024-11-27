package org.triBhaskar.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest registerRequest) {
        CoinUser coinUser = userService.registerUser(registerRequest);
        return ResponseEntity.ok(new RegisterResponse("success", "User registered successfully", coinUser.getUsername(), LocalDateTime.now()));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(userService.loginUser(loginRequest));
    }
    @PostMapping("/test")
    public ResponseEntity<ApiResponse> testmore(@RequestBody String test) {
        return ResponseEntity.ok(new ApiResponse("success", "Tested successfully"));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse> forgotPassword(@RequestBody String email, @RequestBody String resetpwdUrl) {
        System.out.println("enjoo");
            userService.forgotPassword(email.trim());
            return ResponseEntity.ok(new ApiResponse(
                    "success",
                    "If the email exists in our system, a password reset link will be sent"
            ));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse> resetPassword(
            @RequestParam String token,
            @RequestParam String newPassword) {
        try {
            userService.resetPassword(token, newPassword);
            return ResponseEntity.ok(new ApiResponse(
                    "success",
                    "Password has been reset successfully"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse("error", e.getMessage()));
        }
    }
}
