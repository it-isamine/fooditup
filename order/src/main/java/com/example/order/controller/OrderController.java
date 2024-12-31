package com.example.order.controller;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import com.example.order.model.MenuItems;
import com.example.order.model.Order;
import com.example.order.model.Restaurant;
import com.example.order.model.User;
import com.example.order.repo.OrderRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/orders")
public class OrderController {

  @Autowired
  OrderRepo repo;

  RestTemplate restTemplate = new RestTemplate();

  @GetMapping
  @Transactional
  public Iterable<Order> getorderss() {
    return repo.findAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Order> getOrder(@PathVariable int id) {
    return ResponseEntity.status(HttpStatus.OK).body(repo.findById(id).orElseThrow());
  }


  @GetMapping("/user/{status}")
  public Iterable<Order> getMyOrders(@RequestAttribute("userid") String userName, @PathVariable String status) {
  
      Iterable<Order> filteredOrders = repo.findAll().stream()
              .filter(order -> order.getUser().getName().equals(userName)) // Filter by user name
              .filter(order -> "null".equals(status) || "all".equals(status) || order.getStatus().equals(status)) // Filter by status
              .toList();
  
      return filteredOrders;
  }
  

  @GetMapping("recently")
  public List<MenuItems> getItems() {
    return repo.findAll().stream()
        .sorted(Comparator.comparing(Order::getCreatedAt).reversed()) // Sort Orders by createdAt descending
        .flatMap(order -> order.getItems().stream()) // Extract MenuItems from each Order
        .toList(); // Convert the result to a List
  }

  @GetMapping("/get")
  public Iterable<Order> getOrdersOfRestaurant(@RequestAttribute("restaurantid") int restaurantid) {
    return repo.findAll().stream().filter(e -> e.getRestaurant().getId() == restaurantid).toList();
  }
  @GetMapping("status/{status}")
  public Iterable<Order> getOrdrByType(@RequestAttribute("restaurantid") int restaurantid, @PathVariable String status) {
  
      Iterable<Order> filteredOrders = repo.findAll().stream()
              .filter(order -> order.getRestaurant().getId()==restaurantid) // Filter by user name
              .filter(order -> "null".equals(status) || "all".equals(status) || order.getStatus().equals(status)) // Filter by status
              .toList();
  
      return filteredOrders;
  }
  @PostMapping("{id}/updateStatus/{status}")
  public void updatestatus(@RequestAttribute("restaurantid") int restauranid,@PathVariable String status,@PathVariable int id) {
    Order order = repo.findById(id).orElseThrow();
    order.setStatus(status);
    repo.save(order);

  }
  @GetMapping("/getProcessed")
  public Iterable<Order> getOrdersProcessed(@RequestAttribute("restaurantid") int restaurantid) {
    Iterable<Order> orders = repo.findAll().stream().filter(e -> e.getRestaurant().getId() == restaurantid).toList();

    System.out.println(orders);
    return orders;
  }

  @PostMapping
  @Transactional
  public Order addOrder(@RequestBody Order order, @RequestAttribute("userid") String userid) {
    // Validate the incoming order request
    if (order == null) {
      return null;
    }

    User user = order.getUser(); // Default to the user in the order
    Restaurant restaurant = order.getRestaurant(); // Default to the restaurant in the order

    // Try to fetch user from the user microservice
    try {
      ResponseEntity<User> userResponse = restTemplate.exchange(
          "http://localhost:9091/users/users/" + userid,
          HttpMethod.GET,
          null,
          new ParameterizedTypeReference<User>() {
          });

      if (userResponse.getBody() != null) {
        user = userResponse.getBody();
      } else {
        // If user doesn't exist, create the user using POST request
        ResponseEntity<User> userCreatedResponse = restTemplate.exchange(
            "http://localhost:9091/users/users/",
            HttpMethod.POST,
            new HttpEntity<>(user), // Send the user object as the request body
            new ParameterizedTypeReference<User>() {
            });
        if (userCreatedResponse.getBody() != null) {
          user = userCreatedResponse.getBody();
        }
      }
    } catch (Exception e) {
      // If the user service is down, create the user locally and use it
      System.out.println("User service not available, creating user locally: " + e.getMessage());
    }

    // Try to fetch restaurant from the restaurant microservice
    try {
      ResponseEntity<Restaurant> restaurantResponse = restTemplate.exchange(
          "http://localhost:8090/restaurants/" + restaurant.getId(),
          HttpMethod.GET,
          null,
          new ParameterizedTypeReference<Restaurant>() {
          });

      if (restaurantResponse.getBody() != null) {
        restaurant = restaurantResponse.getBody();
      } else {
        // If restaurant doesn't exist, create the restaurant using POST request
        ResponseEntity<Restaurant> restaurantCreatedResponse = restTemplate.exchange(
            "http://localhost:8090/restaurants/",
            HttpMethod.POST,
            new HttpEntity<>(restaurant), // Send the restaurant object as the request body
            new ParameterizedTypeReference<Restaurant>() {
            });
        if (restaurantCreatedResponse.getBody() != null) {
          restaurant = restaurantCreatedResponse.getBody();
        }
      }
    } catch (Exception e) {
      // If the restaurant service is down, create the restaurant locally and use it
      System.out.println("Restaurant service not available, creating restaurant locally: " + e.getMessage());
    }

    // Set the user and restaurant in the order
    order.setUser(user);
    order.setRestaurant(restaurant);

    // Save the order
    Order savedOrder = repo.save(order); // Assuming you have an orderRepository for saving
    return savedOrder;
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> cancelOrder(@PathVariable int id) {
    repo.deleteById(id);
    return ResponseEntity.noContent().build();
  }

}
