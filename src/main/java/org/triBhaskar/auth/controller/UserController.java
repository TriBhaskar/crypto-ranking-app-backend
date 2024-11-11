package org.triBhaskar.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.triBhaskar.auth.entity.CoinUser;
import org.triBhaskar.auth.model.RegisterRequest;
import org.triBhaskar.auth.model.RegisterResponse;
import org.triBhaskar.auth.service.UserService;

import java.time.LocalDateTime;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest registerRequest) {
        CoinUser coinUser = userService.registerUser(registerRequest);
        return ResponseEntity.ok(new RegisterResponse("success", "User registered successfully", coinUser.getId(), LocalDateTime.now()));
    }
}
