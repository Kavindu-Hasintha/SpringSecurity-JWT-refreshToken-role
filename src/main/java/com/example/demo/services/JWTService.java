package com.example.demo.services;

import com.example.demo.entities.RefreshToken;
import com.example.demo.entities.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface JWTService {

    String generateToken(UserDetails userDetails, Long userId);
    String extractUsername(String token);
    boolean isTokenValid(String refreshToken, UserDetails userDetails);
    String generateRefreshToken(User user);
    boolean isRefreshTokenExpired(RefreshToken refreshToken);
}
