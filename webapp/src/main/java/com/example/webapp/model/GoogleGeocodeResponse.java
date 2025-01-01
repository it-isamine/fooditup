package com.example.webapp.model;

import java.util.List;

import lombok.Data;

@Data
public class GoogleGeocodeResponse {
    private List<Result> results;
    private String status;

    // Getters and Setters
    @Data
    public static class Result {
        private Geometry geometry;

        // Getters and Setters
    }

    @Data
    public static class Geometry {
        private Location location;

        // Getters and Setters
    }

    @Data
    public static class Location {
        private double lat;
        private double lng;

        // Getters and Setters
    }
}
