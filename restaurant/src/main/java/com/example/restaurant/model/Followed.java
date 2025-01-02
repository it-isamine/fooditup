package com.example.restaurant.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.Getter;

@Getter
@Entity
@Table(name = "followed")
public class Followed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "userid")
    private User user;

    @ManyToOne
    @JoinColumn(name = "restaurantid")
    private Restaurant restaurant;

    public Followed() {
    }

    public Followed setUser(User user) {
        this.user = user;
        return this;
    }

    public Followed setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
        return this;
    }
}
