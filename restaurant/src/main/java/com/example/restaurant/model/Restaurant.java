package com.example.restaurant.model;

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

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
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
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<RestaurantItem> restaurantItems = new ArrayList<>();

    @Column(name = "createdat")
    private LocalDateTime createdAt;

    @Column(name = "updatedat")
    private LocalDateTime updatedAt;
    @Column(name = "image_url")
    private String imageurl;

    public void addToList(RestaurantItem item) {
        this.restaurantItems.add(item);
    }

    public Restaurant setRestaurantid(int id) {
        this.id = id;
        return this;
    }

    public Restaurant setName(String name) {
        this.name = name;
        return this;
    }

    public Restaurant setrating(Double rating) {
        this.rating = rating;
        return this;
    }

    public Restaurant setusers(List<User> users) {
        this.users = users;
        return this;
    }

    public Restaurant setcreated(LocalDateTime created) {
        this.createdAt = created;
        return this;
    }

    public Restaurant setupdated(LocalDateTime updated) {
        this.updatedAt = updated;
        return this;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", rating=" + rating +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", imageurl='" + imageurl + '\'' +
                '}';
    }

}
