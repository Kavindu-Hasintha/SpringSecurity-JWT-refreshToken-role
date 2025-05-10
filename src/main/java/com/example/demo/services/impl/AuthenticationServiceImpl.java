package com.example.demo.services.impl;

import com.example.demo.dtos.JwtAuthenticationResponse;
import com.example.demo.dtos.NewTokenRequest;
import com.example.demo.dtos.SignInRequest;
import com.example.demo.dtos.SignUpRequest;
import com.example.demo.entities.RefreshToken;
import com.example.demo.entities.User;
import com.example.demo.enums.Role;
import com.example.demo.repo.RefreshTokenRepo;
import com.example.demo.repo.UserRepo;
import com.example.demo.services.AuthenticationService;
import com.example.demo.services.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final RefreshTokenRepo refreshTokenRepo;

    public User signup(SignUpRequest signUpRequest) {
        User user = new User();
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setRole(Role.USER);

        return userRepo.save(user);
    }

    public JwtAuthenticationResponse signin(SignInRequest signinRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                signinRequest.getEmail(), signinRequest.getPassword()));

        var user = userRepo.findByEmail(signinRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password!"));

        var jwt = jwtService.generateToken(user, user.getId());
        var refreshToken = jwtService.generateRefreshToken(user);

        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
        jwtAuthenticationResponse.setToken(jwt);
        jwtAuthenticationResponse.setRefreshToken(refreshToken);
        return jwtAuthenticationResponse;
    }

    public JwtAuthenticationResponse genarateNewTokenUsingRefreshToken(NewTokenRequest newTokenRequest) throws Exception {
        RefreshToken refreshToken = refreshTokenRepo.findByRefreshToken(newTokenRequest.getRefreshToken()).get();
        if (jwtService.isRefreshTokenExpired(refreshToken)) {
            refreshTokenRepo.delete(refreshToken);
            throw new Exception("Refresh token was expired. Please make a new signin request");
        }
        var jwt = jwtService.generateToken(refreshToken.getUser(), refreshToken.getUser().getId());

        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
        jwtAuthenticationResponse.setMessage("New access token created!!!");
        jwtAuthenticationResponse.setToken(jwt);
        jwtAuthenticationResponse.setRefreshToken(newTokenRequest.getRefreshToken());
        return jwtAuthenticationResponse;

    }

    @Transactional
    public String logout(String refreshToken) {
        RefreshToken refreshTokenEntity = refreshTokenRepo.findByRefreshToken(refreshToken).get();
        refreshTokenRepo.delete(refreshTokenEntity);
        return "Refresh token has been deleted.";
    }
}
