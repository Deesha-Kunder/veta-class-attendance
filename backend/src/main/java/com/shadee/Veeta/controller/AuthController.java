package com.shadee.Veeta.controller;

import com.shadee.Veeta.dto.LoginRequest;
import com.shadee.Veeta.dto.LoginResponse;
import com.shadee.Veeta.dto.SignUpRequest;
import com.shadee.Veeta.dto.SignUpResponse;
import com.shadee.Veeta.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")

public class AuthController {
    @Autowired
    private AuthService service;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        LoginResponse response = service.login(loginRequest);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest signUpRequest){
        SignUpResponse response = service.signUp(signUpRequest);
        return ResponseEntity.ok(response);
    }
}
