package com.eccomerce.auth_service.security.util;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TokenUtil {

    private final String SECRET_KEY = "SECRET";
    private final Long EXPIRATION_IN_HOURS = 3600000L;

    public String getUsernameFromToken(String token) {
        String username = null;
        try {
            final Claims claims = this.getClaimsFromToken(token);
            if (claims != null) username = claims.getSubject();
        } catch (Exception e) {
            log.error("Failed to get username from token ", e);
            username = null;
        }
        return username;
    }

    private Claims getClaimsFromToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser().setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.error("Failed to claim from token ", e);
        }
        return claims;

    }

    public Date getExpirationDateFromToken(String token) {
        Date expiration = null;
        try {
            final Claims claims = this.getClaimsFromToken(token);
            if (claims != null) expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

    public String generateToken(UserDetails userDetails) {
        final String authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", authorities);
        return createToken(claims, userDetails.getUsername());
    }

    public Boolean isTokenExpired(String token) {
        final Date expiration = this.getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private String createToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public UsernamePasswordAuthenticationToken getAuthentication(final String token, final Authentication existingAuth, final UserDetails userDetails) {

        final JwtParser jwtParser = Jwts.parser().setSigningKey(SECRET_KEY);

        final Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);

        final Claims claims = claimsJws.getBody();

        final Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("role").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }

    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + this.EXPIRATION_IN_HOURS * 8);
    }

    public Boolean isValidToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

}
