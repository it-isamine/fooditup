package com.example.webapp.service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.webapp.model.Coordinates;
import com.example.webapp.model.GoogleGeocodeResponse;

@Service
public class GeocodingService {

    
    private  RestTemplate restTemplate = new RestTemplate();


    public Coordinates getCoordinates(String address) {
        String apiKey = "AIzaSyAVIF0VrYeqZkKA2H5gvH0p0DvuBsdB3HM"; // Replace with your actual API key
        String url = String.format("https://maps.googleapis.com/maps/api/geocode/json?address=%s&key=%s",
                URLEncoder.encode(address, StandardCharsets.UTF_8), apiKey);

        // Make the API call
        ResponseEntity<GoogleGeocodeResponse> response = restTemplate.getForEntity(url, GoogleGeocodeResponse.class);

        // Parse the response
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            GoogleGeocodeResponse geocodeResponse = response.getBody();
            if (!geocodeResponse.getResults().isEmpty()) {
                GoogleGeocodeResponse.Result result = geocodeResponse.getResults().get(0);
                double lat = result.getGeometry().getLocation().getLat();
                double lng = result.getGeometry().getLocation().getLng();
                return new Coordinates(lat, lng);
            }
        }
        throw new RuntimeException("Failed to fetch coordinates for address: " + address);
    }
}
