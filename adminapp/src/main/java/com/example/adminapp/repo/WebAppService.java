package com.example.adminapp.repo;

import org.springframework.web.client.RestTemplate;
import com.example.adminapp.model.Restaurant;
import com.example.adminapp.model.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class WebAppService {

    @Autowired
    RestTemplate restTemplate;

    public void setVisibility(int id) { // exist

        ResponseEntity<Void> response = restTemplate.exchange("http://localhost:8090/restaurants/visibility/" + id,
                HttpMethod.POST, null, Void.class);

    }

    public Iterable<Restaurant> getRestaurant() { // exist

        ResponseEntity<Iterable<Restaurant>> responseEntity = restTemplate.exchange(
                "http://localhost:8090/restaurants/adminn",
                HttpMethod.GET,
                null, // Pass any required HttpEntity (like headers) here, or null if not needed
                new ParameterizedTypeReference<Iterable<Restaurant>>() {
                });

        return responseEntity.getBody();
    }

    public void updateUser(User user, String token) { // exist
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token); // Add the token to the header
        headers.setContentType(MediaType.APPLICATION_JSON); // Set the content type to JSON

        // Include the order object in the request body
        HttpEntity<User> entity = new HttpEntity<>(user, headers);

        ResponseEntity<Void> response = restTemplate.exchange("http://localhost:8090/users/users",
                HttpMethod.PUT, entity, Void.class);

    }

    public Iterable<User> getUsers() { // exist
        ResponseEntity<Iterable<User>> response = restTemplate.exchange("http://localhost:8090/users/get",
                HttpMethod.GET, null, new ParameterizedTypeReference<Iterable<User>>() {
                });
        return response.getBody();
    }

    public void deleteUser(UUID id, String token) { // exist
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token); // Add the token to the header
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Void> response = restTemplate.exchange("http://localhost:8090/users/users/" + id,
                HttpMethod.DELETE, entity, Void.class);
    }

    public void deleteRestaurant(int id, String token) { // exist
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token); // Add the token to the header
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Void> response = restTemplate.exchange("http://localhost:8090/restaurants/" + id,
                HttpMethod.DELETE, entity, Void.class);
    }

    public User getUser(String token) { // exist
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token); // Add the token to the header
        HttpEntity<String> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        System.out.println("aaaazaz" + headers);
        ResponseEntity<User> response = restTemplate.exchange("http://localhost:8090/users/user/me", HttpMethod.GET,
                entity, User.class);
        System.out.println("Response from gateway: " + response.getBody());
        return response.getBody();
    }

}
