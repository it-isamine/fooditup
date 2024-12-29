package com.example.login.config;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.login.model.CustomUserDetails;
import com.example.login.model.Restaurant;
import com.example.login.model.User;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = restTemplate.getForObject("http://localhost:9091/users/users/" + username, User.class);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        Restaurant restaurant = user.getRestaurant();
        int id = (restaurant != null) ? restaurant.getId() : 0;

        return new CustomUserDetails(user.getName(), user.getPassword(), id, getGrantedAuthorities(user.getRole()));
    }

    private List<GrantedAuthority> getGrantedAuthorities(String role) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        return authorities;
    }
}
