package com.example.webapp.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.example.webapp.model.CustomUserDetails;

@Service
public class JWTService {

        @Autowired
        private JwtEncoder jwtEncoder;

        @Autowired
        RestTemplate restTemplate;

        public String generateToken(Authentication authentication) {
                CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
                Integer restaurantid = customUserDetails.getRestaurantId();
                Instant now = Instant.now();
                JwtClaimsSet claims = JwtClaimsSet.builder()
                                .issuer("self")
                                .issuedAt(now)
                                .expiresAt(now.plus(2, ChronoUnit.DAYS))
                                .subject(authentication.getName())
                                .claim("userid", customUserDetails.getUsername())
                                .claim("restaurantid", restaurantid = (restaurantid != null) ? restaurantid : 0)
                                .build();

                JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters
                                .from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);

                return this.jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
        }

}
