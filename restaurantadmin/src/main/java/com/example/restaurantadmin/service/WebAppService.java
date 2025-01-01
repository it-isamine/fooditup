package com.example.restaurantadmin.service;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.restaurantadmin.model.Itemrest;
import com.example.restaurantadmin.model.MenuItems;
import com.example.restaurantadmin.model.Order;
import com.example.restaurantadmin.model.OrderDto;
import com.example.restaurantadmin.model.Restaurant;
import com.example.restaurantadmin.model.User;
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

    public void updateUser(User user, String token) { // exist
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token); // Add the token to the header
        headers.setContentType(MediaType.APPLICATION_JSON); // Set the content type to JSON

        // Include the order object in the request body
        HttpEntity<User> entity = new HttpEntity<>(user, headers);

        ResponseEntity<Void> response = restTemplate.exchange("http://localhost:8090/users/users",
                HttpMethod.PUT, entity, Void.class);

    }

    public void deleteUser(UUID id, String token) { // exist
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token); // Add the token to the header
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Void> response = restTemplate.exchange("http://localhost:8090/users/users/" + id,
                HttpMethod.DELETE, entity, Void.class);
    }

    public Iterable<OrderDto> getOrderOfRestaurant(String token) { // exist
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token); // Add the token to the header
        HttpEntity<String> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Iterable<OrderDto>> response = restTemplate.exchange("http://localhost:8090/orders/getProcessed",
                HttpMethod.GET, entity, new ParameterizedTypeReference<Iterable<OrderDto>>() {

                });
        return response.getBody();
    }

    public Iterable<Order> getOrderOfRestaurantByType(String token, String status) { // exist
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token); // Add the token to the header
        HttpEntity<String> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Iterable<Order>> response = restTemplate.exchange(
                "http://localhost:8090/orders/status/" + status,
                HttpMethod.GET, entity, new ParameterizedTypeReference<Iterable<Order>>() {

                });
        return response.getBody();
    }

    public void updateOrder(String token, String status, int id) { // exist
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token); // Add the token to the header
        HttpEntity<String> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:8090/orders/" + id + "/updateStatus/" + status,
                HttpMethod.POST, entity, Void.class);
    }

    public Iterable<User> getUsersOfRestaurant(String token) { // exist
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token); // Add the token to the header
        HttpEntity<String> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Iterable<User>> response = restTemplate.exchange("http://localhost:8090/restaurants/getUsers",
                HttpMethod.GET, entity, new ParameterizedTypeReference<Iterable<User>>() {

                });
        return response.getBody();
    }

    public Iterable<MenuItems> getItems(String token) { // exist
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

    public void addEmployee(String token, User user) { // exist
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token); // Add the token to the header
        headers.setContentType(MediaType.APPLICATION_JSON); // Set the content type to JSON

        // Include the order object in the request body
        HttpEntity<User> entity = new HttpEntity<>(user, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity response = restTemplate.exchange("http://localhost:8090/restaurants/addEmployee",
                HttpMethod.POST, entity, Void.class);

    }

    public void addItem(String token, Itemrest item) { // exist
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token); // Add the token to the header
        headers.setContentType(MediaType.APPLICATION_JSON); // Set the content type to JSON

        // Include the order object in the request body
        HttpEntity<Itemrest> entity = new HttpEntity<>(item, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity response = restTemplate.exchange("http://localhost:8090/restaurants/addtomenu", HttpMethod.POST,
                entity, Void.class);

    }

    public void deleteItem(String token, int id) { // exist
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token); // Add the token to the header
        HttpEntity<String> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Void> response = restTemplate.exchange("http://localhost:8090/restaurants/item/" + id,
                HttpMethod.DELETE, entity, Void.class);
    }

    public Iterable<MenuItems> getItemz(String token) { // exist
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
