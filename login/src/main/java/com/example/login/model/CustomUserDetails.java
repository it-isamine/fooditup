package com.example.login.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import java.util.Collection;

public class CustomUserDetails extends User {

    private final int restaurantId;

    public CustomUserDetails(String username, String password, int restaurantId,
            Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.restaurantId = restaurantId;
    }

    public int getRestaurantId() {
        return restaurantId;
    }
}
