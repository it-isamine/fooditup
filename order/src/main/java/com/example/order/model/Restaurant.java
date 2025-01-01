package com.example.order.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.Data;
import lombok.ToString;

@Entity
@Data
@Table(name = "restaurants")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "restaurantid")
    private int id;

    @Column(name = "name")
    private String name;

    private String address;

    private String phone;

    private Double rating;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "restaurantid")
    private List<User> users;

    @OneToMany(mappedBy = "restaurant")
    @ToString.Exclude
    @JsonIgnore
    private List<RestaurantItem> restaurantItems = new ArrayList<>();

    @Column(name = "createdat")
    private LocalDateTime createdAt;

    @Column(name = "updatedat")
    private LocalDateTime updatedAt;
}
