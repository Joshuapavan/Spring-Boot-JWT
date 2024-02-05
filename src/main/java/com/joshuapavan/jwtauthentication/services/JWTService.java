package com.joshuapavan.jwtauthentication.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

public interface JWTService {

    String extractUsername(String token);
    String generateToken(UserDetails userDetails);
    boolean isTokenValid(String token);

}
