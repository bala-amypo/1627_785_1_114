// package com.example.demo.config;

// import io.jsonwebtoken.Claims;
// import io.jsonwebtoken.Jwts;
// import io.jsonwebtoken.JwtException;
// import io.jsonwebtoken.SignatureAlgorithm;
// import io.jsonwebtoken.security.Keys;
// import org.springframework.stereotype.Component;

// import java.security.Key;
// import java.util.Date;

// @Component
// public class JwtTokenProvider {

//     private final long jwtExpirationMs;
//     private final Key key;

//     // ✅ Constructor expected by tests
//     public JwtTokenProvider(String secret, int jwtExpirationMs) {
//         this.jwtExpirationMs = jwtExpirationMs;
//         this.key = Keys.hmacShaKeyFor(secret.getBytes());
//     }

//     // ✅ Default constructor for Spring
//     public JwtTokenProvider() {
//         this.jwtExpirationMs = 60 * 60 * 1000; // 1 hour
//         this.key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
//     }

//     public String generateToken(Long userId, String username, String role) {
//         Date now = new Date();
//         Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

//         return Jwts.builder()
//                 .setSubject(username)
//                 .claim("userId", userId)
//                 .claim("role", role)
//                 .setIssuedAt(now)
//                 .setExpiration(expiryDate)
//                 .signWith(key)
//                 .compact();
//     }

//     public Claims getClaims(String token) {
//         return Jwts.parserBuilder()
//                 .setSigningKey(key)
//                 .build()
//                 .parseClaimsJws(token)
//                 .getBody();
//     }

//     public String getUsernameFromToken(String token) {
//         return getClaims(token).getSubject();
//     }

//     public boolean validateToken(String token) {
//         try {
//             getClaims(token);
//             return true;
//         } catch (JwtException | IllegalArgumentException e) {
//             return false;
//         }
//     }
// }


package com.example.demo.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.security.Key;

@Component
public class JwtTokenProvider {
    private final String secretKey;
    private final long validityInMilliseconds;

    public JwtTokenProvider() {
        this.secretKey = "ChangeThisSecretKeyReplaceMe1234567890";
        this.validityInMilliseconds = 3600000;
    }

    public JwtTokenProvider(String secretKey, long validity) {
        this.secretKey = secretKey;
        this.validityInMilliseconds = validity;
    }

    public String generateToken(Long id, String email, String role) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(id));
        claims.put("email", email);
        claims.put("role", role);
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey.getBytes()).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Claims getClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey.getBytes()).build().parseClaimsJws(token).getBody();
    }
}