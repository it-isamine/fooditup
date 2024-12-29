package com.example.login.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.login.model.CustomUserDetails;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private JWTService jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String token = jwtUtil.generateToken(userDetails);
        System.out.println(token);

        String redirectUrl;
        if (userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_CUSTOMER"))) {
            redirectUrl = "http://localhost:9771/home";
        } else {
            redirectUrl = "http://localhost:9772/admin/home";
        }

        String encodedRedirectUrl = URLEncoder.encode(redirectUrl, StandardCharsets.UTF_8.toString());
        String clientRedirectUrl = "http://localhost:9771/home";

        // Set the JWT token in a secure, HTTP-only cookie
        response.setHeader("Set-Cookie", "auth_token=" + token + "; HttpOnly; Secure; SameSite=Strict; Path=/");

        // Redirect to the client-side handler
        response.sendRedirect(redirectUrl);
    }
}
