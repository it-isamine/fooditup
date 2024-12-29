package com.example.restaurant.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "restaurantmenuitems")
public class RestaurantItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "restaurantid")
    @JsonIgnore
    private Restaurant restaurant;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "menuid")
    private MenuItems item;

    private Double price;

    public RestaurantItem() {
    }

    public RestaurantItem(Restaurant restaurant, Double price, MenuItems item) {
        this.restaurant = restaurant;
        this.price = price;
        this.item = item;
    }
    // Getters and Setters
}
