package sample.baro.auth.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sample.baro.auth.CustomUserDetails;
import sample.baro.domain.Role;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

    private final SecretKey secretKey;
    @Value("${jwt.expiration-ms}")
    private long TOKEN_EXPIRATION;

    public JwtUtil(@Value("${jwt.secret-key}") String key) {

        this.secretKey = Keys.hmacShaKeyFor(key.getBytes());
    }

    public String generateAccessToken(CustomUserDetails userDetails) {
        return generateToken(userDetails.getUsername(), userDetails.getRole());
    }

    private String generateToken(String username, Role role) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + TOKEN_EXPIRATION);
        return Jwts.builder().setSubject(username)
                .claim("role", role.name())
                .signWith(secretKey)
                .setIssuedAt(now)
                .setExpiration(exp)
                .compact();
    }

    public String getRole(String token) {
        JwtParser parser = Jwts.parser().verifyWith(secretKey).build();

        return parser.parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
    }

    public String getUsername(String token) {
        JwtParser parser = Jwts.parser().verifyWith(secretKey).build();
        return parser.parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("만료된 토큰입니다: {}", e.getMessage());
        } catch (JwtException e) {
            log.warn("유효하지 않은 토큰입니다: {}", e.getMessage());
        } catch (Exception e) {
            log.warn("알수 없는 오류 예외 발생: {}", e.getMessage());
        }
        return false;
    }
}
