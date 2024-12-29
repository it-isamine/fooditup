package com.example.webapp.model;

import lombok.Data;

@Data
public class RestaurantItem {

    private Long id;
    private Restaurant restaurant;
    private MenuItems item;
    private Double price;

    // Getters and Setters
}
