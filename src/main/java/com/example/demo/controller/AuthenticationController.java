package com.example.demo.controller;

import com.example.demo.dtos.JwtAuthenticationResponse;
import com.example.demo.dtos.NewTokenRequest;
import com.example.demo.dtos.SignInRequest;
import com.example.demo.dtos.SignUpRequest;
import com.example.demo.entities.User;
import com.example.demo.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody SignUpRequest signUpRequest) {
        return ResponseEntity.ok(authenticationService.signup(signUpRequest));
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody SignInRequest signInRequest) {
        return ResponseEntity.ok(authenticationService.signin(signInRequest));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtAuthenticationResponse> refresh(@RequestBody NewTokenRequest newTokenRequest) {
        try {
            return ResponseEntity.ok(authenticationService.genarateNewTokenUsingRefreshToken(newTokenRequest));
        } catch (Exception e) {
            JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
            jwtAuthenticationResponse.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(jwtAuthenticationResponse);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody NewTokenRequest refreshToken) {
        return ResponseEntity.ok(authenticationService.logout(refreshToken.getRefreshToken()));
    }
}
