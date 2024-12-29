package com.example.user.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private String secretKey = "your-very-secure-32-character-minimum-secret-key";// Replace with your actual secret
                                                                                  // key.
    @Autowired
    JwtDecoder jwtDecoder;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws IOException, ServletException {
        String authHeader = request.getHeader("Authorization");
        System.out.println("éééééééééééé" + authHeader + "ééééééé");
        String path = request.getRequestURI();
        // Skip JWT authentication for public paths
        if (path.startsWith("/users//users/verify") || path.equals("/login")) {
            filterChain.doFilter(request, response);
            return;
        }
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            throw new IllegalArgumentException("Invalid or missing Authorization header"); // No JWT found in the
                                                                                           // request, continue filter
                                                                                           // chain.
        }

        String jwt = authHeader.substring(7); // Remove "Bearer " prefix.
        Claims claims = extractClaims(jwt);
        request.setAttribute("userid", claims.get("userid"));
        request.setAttribute("restaurantid", claims.get("restaurantid"));
        filterChain.doFilter(request, response);
    }

    public Claims extractClaims(String token) {
        // Use the JWT library to parse the token and retrieve the claims
        return Jwts.parser()
                .setSigningKey(secretKey.getBytes()) // Set the signing key
                .build()
                .parseClaimsJws(token)
                .getBody(); // Returns Claims object
    }

}
