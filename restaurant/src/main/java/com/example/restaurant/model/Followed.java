package com.example.restaurant.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;

@Getter
@Entity
@Table(name = "followed")
public class Followed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @ManyToOne
    @JoinColumn(name = "userid")
    User user;

    @ManyToOne
    @JoinColumn(name = "restaurantid")
    Restaurant restaurant;

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
