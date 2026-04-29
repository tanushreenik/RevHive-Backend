package com.project.revhive.demo.service;


import com.project.revhive.demo.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.InvalidKeyException;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {
    private final String SECRET_KEY = "mySuperSecretKeyThatIsAtLeast32BytesLong!";

    private final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    public String generateToken(User user) throws InvalidKeyException
    {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("role",user.getRole().name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+100*60*60))
                .signWith(key)
                .compact();
    }

    public SecretKey secretKey()
    {
        return key;
    }

}
