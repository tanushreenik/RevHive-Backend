package com.project.revhive.demo.security;


import com.project.revhive.demo.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.InvalidKeyException;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {
    private final String SECRET_KEY = "mySuperSecretKeyThatIsAtLeast32BytesLong!";



    public SecretKey getSigningKey()
    {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(User user) throws InvalidKeyException
    {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("role",user.getRole().name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+100*60*60))
                .signWith(getSigningKey())
                .compact();
    }
//
//    public String extractUsername(String token)
//    {
//        return getClaims(token).getSubject();
//    }

//    private Claims getClaims(String token)
//    {
//        return Jwts.parser().
//    }



}
