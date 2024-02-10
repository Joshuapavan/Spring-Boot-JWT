package com.joshuapavan.jwtauthentication.services.impl;

import com.joshuapavan.jwtauthentication.services.JWTService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JWTServiceImpl implements JWTService {


    // Creates JWT tokens
    public String generateToken(UserDetails userDetails){
        // create a JWT token and return it as a string
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
//                .setSubject(userDetails.getPassword())
                .setExpiration(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSignatureKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    // to get signature key
    private Key getSignatureKey() {
        byte[] key = Decoders.BASE64URL.decode("");
        return Keys.hmacShaKeyFor(key);
    }

    // method to extract claims (data from the token) and return the data
    private <T> T extractClaim(String token, Function<Claims, T> claimResolvers){
        // gets all the claims from token
        final Claims claims = extractAllClaims(token);
        // returns the required claim
        return claimResolvers.apply(claims);
    }

    // method to get all the claims from the token
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignatureKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // will call the above defined methods to get the userName from token
    public String extractUserName(String token){
        return extractClaim(token, Claims::getAudience);
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExipired(token));
    }

    private boolean isTokenExipired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }
}
