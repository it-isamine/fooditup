package com.example.webapp.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class Restaurant {

    private int id;
    private String name;
    private String address;
    private String phone;
    private Double rating;
    private List<User> users;
    private List<RestaurantItem> restaurantItems = new ArrayList<>();
    private String imageurl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
