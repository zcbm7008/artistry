package com.artistry.artistry.auth.jwt;


import com.artistry.artistry.Exceptions.ArtistryInvalidValueException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private final SecretKey secretKey;
    private final long validityInMilliSeconds;
    private final String EMAIL_KEY = "email";
    public JwtTokenProvider(@Value("${security.jwt.token.secret-key}") String secretKey,
                            @Value("${security.jwt.token.expire-length}") long validityInMilliSeconds){
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.validityInMilliSeconds = validityInMilliSeconds;
    }

    public String generateToken(final String key, final String value) {
        final Date now = new Date();
        final Date expiryDate = new Date(now.getTime() + validityInMilliSeconds);

        return Jwts.builder()
                .claim(key, value)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }

    public String extractValueFromToken(String key, String token) {
        final Date now = new Date();
        final Date expiryDate = new Date(now.getTime() + validityInMilliSeconds);

        Jws<Claims> claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token);

        return claims.getPayload().get(key, String.class);
    }

    public String generateEmailToken(final String email) {
        return generateToken(EMAIL_KEY, email);
    }

    public String extractEmailFromToken(String token){
        return extractValueFromToken(EMAIL_KEY,token);

    }

    private void validateAccessToken(final String token){
        try{
            final Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (MalformedJwtException | UnsupportedJwtException e){
            final String logMessage = "잘못된 액세스 토큰 : " + token;

            throw new ArtistryInvalidValueException(logMessage);
        } catch ( ExpiredJwtException e){
            throw new ExpiredJwtException(null,null,"ACCESS_TOKEN_EXPIRED");
        }
    }
}
