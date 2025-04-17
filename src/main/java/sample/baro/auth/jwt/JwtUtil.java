package sample.baro.auth.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey secretKey;
    @Value("${jwt.expiration-ms}")
    private long TOKEN_EXPIRATION;

    public JwtUtil(@Value("${jwt.secret-key}") String key) {

        this.secretKey = Keys.hmacShaKeyFor(key.getBytes());
    }

    public String generateAccessToken(UserDetails userDetails) {
        return generateToken(userDetails.getUsername());
    }

    private String generateToken(String username) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + TOKEN_EXPIRATION);
        return Jwts.builder().setSubject(username)
                .signWith(secretKey)
                .setIssuedAt(now)
                .setExpiration(exp)
                .compact();
    }


}
