package com.example.restaurantadmin.model;

import java.util.UUID;
import lombok.Data;

@Data
public class User {

    UUID id;
    String name;
    String email;
    String password;
    String role;
    Restaurant restaurant;

    public User() {
    }

    public User(String name, String email, String password) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = "CUSTOMER";

    }
}
