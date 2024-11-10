package org.triBhaskar.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.triBhaskar.auth.entity.CoinUser;
import org.triBhaskar.auth.model.RegisterRequest;
import org.triBhaskar.auth.service.UserService;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public CoinUser register(@RequestBody RegisterRequest registerRequest) {
        return userService.registerUser(registerRequest);
    }
}
