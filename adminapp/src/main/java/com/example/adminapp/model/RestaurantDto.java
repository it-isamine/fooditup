package com.example.adminapp.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RestaurantDto {

    private int id;

    private String name;

    private String address;

    private String phone;

    private Double rating;


    private List<User> users = new ArrayList<>();

 
    private List<RestaurantItem> restaurantItems = new ArrayList<>();

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
    private String imageurl;

    public void addToList(RestaurantItem item) {
        this.restaurantItems.add(item);
    }

    public RestaurantDto setRestaurantid(int id) {
        this.id = id;
        return this;
    }

    public RestaurantDto setName(String name) {
        this.name = name;
        return this;
    }

    public RestaurantDto setrating(Double rating) {
        this.rating = rating;
        return this;
    }

    public RestaurantDto setusers(List<User> users) {
        this.users = users;
        return this;
    }

    public RestaurantDto setcreated(LocalDateTime created) {
        this.createdAt = created;
        return this;
    }

    public RestaurantDto setupdated(LocalDateTime updated) {
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
