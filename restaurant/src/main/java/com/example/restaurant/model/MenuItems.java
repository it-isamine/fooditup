package com.example.restaurant.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "menuitems")
public class MenuItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menuid")
    private int id;

    private String name;

    private String description;

    @OneToMany(mappedBy = "item")
    @JsonIgnore
    private List<RestaurantItem> restaurantItems = new ArrayList<>();

    private Boolean availability;
    
    @Column(name = "image_url")
    private String imageurl;

    @ManyToMany(mappedBy = "items")
    List<Sides> sides;

    @PrePersist
    public void assignRandomId() {
        if (id == 0) { // Check for the default value of 0
            id = new Random().nextInt(Integer.MAX_VALUE); // Generate a positive random int
        }
    }
}
