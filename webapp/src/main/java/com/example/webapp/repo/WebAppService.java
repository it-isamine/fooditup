package com.example.webapp.repo;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.webapp.model.Itemrest;
import com.example.webapp.model.MenuItems;
import com.example.webapp.model.Order;
import com.example.webapp.model.OrderDto;
import com.example.webapp.model.Restaurant;
import com.example.webapp.model.RestaurantDto;
import com.example.webapp.model.RestaurantItem;
import com.example.webapp.model.Sides;
import com.example.webapp.model.User;
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

    private static final String API_GATEWAY_URL = "http://localhost:8090/restaurants/user-info";

    public Iterable<RestaurantDto> getRestaurant() {

        ResponseEntity<Iterable<RestaurantDto>> responseEntity = restTemplate.exchange(
                "http://localhost:8090/restaurants/getresto",
                HttpMethod.GET,
                null, // Pass any required HttpEntity (like headers) here, or null if not needed
                new ParameterizedTypeReference<Iterable<RestaurantDto>>() {
                });

        return responseEntity.getBody();
    }

    public Iterable<RestaurantDto> getRestaurantDto(String token) { // exist
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token); // Add the token to the header
        System.out.println("Using token: " + token);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Iterable<RestaurantDto>> responseEntity = restTemplate.exchange(
                "http://localhost:8090/restaurants",
                HttpMethod.GET,
                entity, // Pass any required HttpEntity (like headers) here, or null if not needed
                new ParameterizedTypeReference<Iterable<RestaurantDto>>() {
                });
        System.out.println(responseEntity.getBody());

        return responseEntity.getBody();
    }

    public void updateUser(User user, String token) { // exist
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token); // Add the token to the header
        headers.setContentType(MediaType.APPLICATION_JSON); // Set the content type to JSON

        // Include the order object in the request body
        HttpEntity<User> entity = new HttpEntity<>(user, headers);

        ResponseEntity<Void> response = restTemplate.exchange("http://localhost:8090/users/self",
                HttpMethod.PUT, entity, Void.class);

    }

    public MenuItems getItem(int id) { // exist
        ResponseEntity<MenuItems> responseEntity = restTemplate.exchange(
                "http://localhost:8090/restaurants/menu/" + id,
                HttpMethod.GET,
                null, MenuItems.class);
        return responseEntity.getBody();
    }

    public Restaurant gRestaurant(int id) { // exist
        ResponseEntity<Restaurant> responseEntity = restTemplate.exchange(
                "http://localhost:8090/restaurants/" + id,
                HttpMethod.GET,
                null, Restaurant.class);
        return responseEntity.getBody();
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

    public Iterable<OrderDto> getORdersOfUser(String token, String status) { // exist
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token); // Add the token to the header
        HttpEntity<String> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Iterable<OrderDto>> response = restTemplate.exchange("http://localhost:8090/orders/user/" + status,
                HttpMethod.GET, entity, new ParameterizedTypeReference<Iterable<OrderDto>>() {
                });
        System.out.println("Response from gateway: " + response.getBody());
        return response.getBody();
    }

    public Order getOrder(String token, int id) { // exist
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token); // Add the token to the header
        HttpEntity<String> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Order> response = restTemplate.exchange("http://localhost:8090/orders/" + id,
                HttpMethod.GET, entity, Order.class);
        System.out.println("Response from gateway: " + response.getBody());
        return response.getBody();
    }

    public void followRestaurant(String token, int restaurantid) { // exist
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token); // Add the token to the header
        System.out.println(headers.getFirst("Authorization"));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:8090/restaurants/follow/" + restaurantid, HttpMethod.POST, entity, Void.class);

    }

    public void unfollowRestaurant(String token, int restaurantid) { // exist
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token); // Add the token to the header
        HttpEntity<String> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:8090/restaurants/unfollow/" + restaurantid, HttpMethod.DELETE, entity, Void.class);

    }

    public Order addOrder(String token, Order orderToSend) { // exist
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token); // Add the token to the header
        headers.setContentType(MediaType.APPLICATION_JSON); // Set the content type to JSON

        // Include the order object in the request body
        HttpEntity<Order> entity = new HttpEntity<>(orderToSend, headers);
        RestTemplate restTemplate = new RestTemplate();

        // Use POST instead of GET to send the order
        ResponseEntity<Order> response = restTemplate.exchange(
                "http://localhost:8090/orders",
                HttpMethod.POST,
                entity,
                Order.class);

        System.out.println("Response from gateway: " + response.getBody());
        return response.getBody();
    }

    public Iterable<RestaurantItem> getItems(String token) { // exist
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token); // Add the token to the header
        HttpEntity<String> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Iterable<RestaurantItem>> response = restTemplate.exchange(
                "http://localhost:8090/restaurants/frenquently/itemz", HttpMethod.GET, entity,
                new ParameterizedTypeReference<Iterable<RestaurantItem>>() {
                });
        return response.getBody();
    }

    public Iterable<RestaurantItem> getItemRest(String token, int id) { // exist
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token); // Add the token to the header
        HttpEntity<String> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Iterable<RestaurantItem>> response = restTemplate.exchange(
                "http://localhost:8090/restaurants/" + id + "/items", HttpMethod.GET, entity,
                new ParameterizedTypeReference<Iterable<RestaurantItem>>() {
                });
        return response.getBody();
    }
    public Iterable<RestaurantItem> getRestoItemzz() {
        ResponseEntity<Iterable<RestaurantItem>> response = restTemplate.exchange("http://localhost:8090/restaurants/frequently", HttpMethod.GET,null,new ParameterizedTypeReference<Iterable<RestaurantItem>>() {
        });
        return response.getBody();

    }
    public Iterable<Sides> getSides() {
        ResponseEntity<Iterable<Sides>> response = restTemplate.exchange("http://localhost:8090/restaurants/getSides", HttpMethod.GET,null,new ParameterizedTypeReference<Iterable<Sides>>() { 
        });
        return response.getBody();
    }
    public Sides getSide(int id) {
        ResponseEntity<Sides> response = restTemplate.exchange("http://localhost:8090/restaurants/getSide/"+id, HttpMethod.GET,null,Sides.class);
        return response.getBody();

    }

    public Iterable<RestaurantItem> getrestoItemz(String token, int restaurantid) { // exist
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token); // Add the token to the header
        HttpEntity<String> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Iterable<RestaurantItem>> response = restTemplate.exchange(
                "http://localhost:8090/restaurants/" + restaurantid + "/getitemz", HttpMethod.GET, entity,
                new ParameterizedTypeReference<Iterable<RestaurantItem>>() {
                });
        return response.getBody();
    }

}
