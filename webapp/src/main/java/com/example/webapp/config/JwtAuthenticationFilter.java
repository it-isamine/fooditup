package com.example.webapp.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.webapp.controller.WebAppController;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private String secretKey = "your-very-secure-32-character-minimum-secret-key";

    @Autowired
    JwtDecoder jwtDecoder;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws IOException, ServletException {
        String authHeader = request.getHeader("Authorization");
        System.out.println(authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(7);
        System.out.println(jwt);
        Claims claims = extractClaims(jwt);

        log.info("" + claims.get("userid"));
        log.info("" + claims.get("restaurantid"));

        request.setAttribute("userid", claims.get("userid"));
        request.setAttribute("restaurantid", claims.get("restaurantid"));
        filterChain.doFilter(request, response);
    }

    public Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey.getBytes()) // Set the signing key
                .build()
                .parseClaimsJws(token)
                .getBody(); // Returns Claims object
    }

    public Claims getClaimsFromToken(String token) {
        try {
            // Decode the JWT token
            Jwt jwt = jwtDecoder.decode(token);
            // Extract claims from the decoded JWT (claims are in the "body" of the token)
            Claims claims = (Claims) jwt.getClaims();
            return claims;
        } catch (Exception e) {
            // Handle the case where the token is invalid or expired
            log.info("Invalid token or expired token");
            return null;
        }
    }

    private Claims validateTokenAndGetClaims(String token) {
        try {
            // Use the JwtParserBuilder correctly in newer versions (v0.11 and above)
            JwtParser parser = Jwts.parser()
                    .setSigningKey(secretKey)
                    .build(); // Build the JwtParser
            return parser.parseClaimsJws(token).getBody(); // Parse the token and extract claims
        } catch (ExpiredJwtException e) {
            log.info("Token expired: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT: " + e.getMessage());
        } catch (MalformedJwtException e) {
            log.info("Malformed JWT: " + e.getMessage());
        } catch (SignatureException e) {
            log.info("Invalid signature: " + e.getMessage());
        } catch (Exception e) {
            log.info("JWT parsing error: " + e.getMessage());
        }
        return null; // Return null if the token is invalid
    }

}
