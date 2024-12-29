package com.example.restaurant.model;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class RestaurantDto {
    
    private int restaurantid;
    private String name;
    private String address;
    private String phone;
    private Double rating;
    private List<RestaurantItemDTO> restaurantItems;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String imageurl;
    private boolean followedByCurrentUser;
    // Getters and setters
}
