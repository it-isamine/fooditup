package com.example.restaurantadmin.model;

import java.util.UUID;
import lombok.Data;

@Data
public class User {

    private UUID id;
    private String name;
    private String email;
    private String password;
    private String role;
    private Restaurant restaurant;

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
