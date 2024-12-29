package com.example.adminapp.repo;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.adminapp.model.MenuItems;

import com.example.adminapp.model.Restaurant;

import com.example.adminapp.model.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class WebAppService {

    @Autowired
    RestTemplate restTemplate;

    private static final String API_GATEWAY_URL = "http://localhost:8090/restaurants/user-info";

    public String sendRequestWithToken(String token) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token); // Add the token to the header
        System.out.println("Using token: " + token);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    API_GATEWAY_URL, // Replace with the actual URL
                    HttpMethod.GET,
                    entity,
                    String.class);
            System.out.println("Response from gateway: " + response.getBody());
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            // Log the error and rethrow or handle accordingly
            System.err.println("HTTP error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            return "Error: " + e.getMessage();
        } catch (Exception e) {
            // Handle other exceptions
            System.err.println("An error occurred: " + e.getMessage());
            return "An unexpected error occurred.";
        }
    }

    public Iterable<Restaurant> getRestaurant() {

        ResponseEntity<Iterable<Restaurant>> responseEntity = restTemplate.exchange(
                "http://localhost:8090/restaurants/admin",
                HttpMethod.GET,
                null, // Pass any required HttpEntity (like headers) here, or null if not needed
                new ParameterizedTypeReference<Iterable<Restaurant>>() {
                });

        return responseEntity.getBody();
    }

    public void addUser(User user) {
        HttpEntity<User> entity = new HttpEntity<>(user);
        ResponseEntity<Void> response = restTemplate.exchange("http://localhost:8090/users/createaccount",
                HttpMethod.POST, entity, Void.class);

    }

    public void updateUser(User user, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token); // Add the token to the header
        headers.setContentType(MediaType.APPLICATION_JSON); // Set the content type to JSON

        // Include the order object in the request body
        HttpEntity<User> entity = new HttpEntity<>(user, headers);

        ResponseEntity<Void> response = restTemplate.exchange("http://localhost:8090/users/users",
                HttpMethod.PUT, entity, Void.class);

    }

    public Iterable<User> getUsers() {
        ResponseEntity<Iterable<User>> response = restTemplate.exchange("http://localhost:8090/users/get",
                HttpMethod.GET, null, new ParameterizedTypeReference<Iterable<User>>() {
                });
        return response.getBody();
    }

    public MenuItems getItem(int id) {
        ResponseEntity<MenuItems> responseEntity = restTemplate.exchange(
                "http://localhost:8090/restaurants/menu/" + id,
                HttpMethod.GET,
                null, MenuItems.class);
        return responseEntity.getBody();
    }

    public void deleteUser(UUID id, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token); // Add the token to the header
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Void> response = restTemplate.exchange("http://localhost:8090/users/users/" + id,
                HttpMethod.DELETE, entity, Void.class);
    }

    public void deleteRestaurant(int id, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token); // Add the token to the header
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Void> response = restTemplate.exchange("http://localhost:8090/restaurants/" + id,
                HttpMethod.DELETE, entity, Void.class);
    }

    public Restaurant gRestaurant(int id) {
        ResponseEntity<Restaurant> responseEntity = restTemplate.exchange(
                "http://localhost:8090/restaurants/" + id,
                HttpMethod.GET,
                null, Restaurant.class);
        return responseEntity.getBody();
    }

    public User getUser(String token) {
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

    public Iterable<User> getUsersOfRestaurant(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token); // Add the token to the header
        HttpEntity<String> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Iterable<User>> response = restTemplate.exchange("http://localhost:8090/restaurants/getUsers",
                HttpMethod.GET, entity, new ParameterizedTypeReference<Iterable<User>>() {

                });
        return response.getBody();
    }

    public Iterable<MenuItems> getItems(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token); // Add the token to the header
        HttpEntity<String> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Iterable<MenuItems>> response = restTemplate.exchange(
                "http://localhost:8090/restaurants/frenquently/items", HttpMethod.GET, entity,
                new ParameterizedTypeReference<Iterable<MenuItems>>() {
                });
        return response.getBody();
    }

    public Iterable<MenuItems> getrestoItems(String token, int restaurantid) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token); // Add the token to the header
        HttpEntity<String> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Iterable<MenuItems>> response = restTemplate.exchange(
                "http://localhost:8090/restaurants/" + restaurantid + "/getitems", HttpMethod.GET, entity,
                new ParameterizedTypeReference<Iterable<MenuItems>>() {
                });
        return response.getBody();
    }

    public Iterable<MenuItems> getItemz(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token); // Add the token to the header
        HttpEntity<String> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Iterable<MenuItems>> response = restTemplate.exchange("http://localhost:8090/restaurants/items",
                HttpMethod.GET, entity, new ParameterizedTypeReference<Iterable<MenuItems>>() {

                });
        return response.getBody();
    }

}
