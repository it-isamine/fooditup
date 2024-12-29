package com.example.webapp.model;

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
    private List<MenuItems> restaurantItems;
    private LocalDateTime createdAt;
    private String imageurl;
    private LocalDateTime updatedAt;
    private boolean followedByCurrentUser;
    // Getters and setters
}
