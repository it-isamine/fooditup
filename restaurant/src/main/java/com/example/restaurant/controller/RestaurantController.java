package com.example.restaurant.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.spi.LoggerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.example.restaurant.repo.*;
import com.fasterxml.jackson.databind.annotation.JsonAppend.Attr;
import com.example.restaurant.adminrepo.AdminRepo;
import com.example.restaurant.model.*;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {
  Logger logger = LoggerFactory.getLogger(RestaurantController.class);
  @Autowired
  RestaurantRepo repo;

  @Autowired
  MenuRepo mRepo;

  @Autowired
  RestTemplate restTemplate;

  @Autowired
  RestaurantItemRepo restaurantItemRepo;

  @Autowired
  PasswordEncoder passwordEncoder;

  @Autowired
  SideRepo sRepo;

  @Autowired
  FollowedRepo fRepo;
  @Autowired
  AdminRepo aRepo;
  @Autowired
  SideMenuRepo sideMenuRepo;

  @GetMapping("/adminn") //exist
  public Iterable<com.example.restaurant.adminmodel.Restaurant> gett() {
    return aRepo.findAll();
  }
  @GetMapping("/getall")
  public Iterable<RestaurantItem> getall() {
    return restaurantItemRepo.findAll();
  }
  @GetMapping("/getSides")
  public Iterable<Sides> getSides() {
    return sRepo.findAll();
  }
  @GetMapping("/getSide/{id}")
  public Sides getSides(@PathVariable int id) {
    return sRepo.findById(id).orElseThrow();
  }
  @PostMapping("/{itemId}/{sideId}")
  public SideMenu addedit(@PathVariable("itemId") int itemid,@PathVariable("sideId") int sideId) {
    SideMenu sideMenu  = new SideMenu();
    sideMenu.setMenuid(itemid);
    sideMenu.setSideid(sideId);
    sideMenuRepo.save(sideMenu);
    return sideMenu;
  }


  @PostMapping("/visibility/{id}") //exist
  public void isenable(@PathVariable int id) {
    com.example.restaurant.adminmodel.Restaurant restau = aRepo.findById(id).orElseThrow();
    boolean visibility = !restau.getEnabled();
    restau.setEnabled(visibility);
    aRepo.save(restau);

  }

  @GetMapping("/getresto")//exist
  public Iterable<RestaurantDto> getResto() {
    List<RestaurantDto> restaurants = repo.findAll()
        .stream()
        .map(restaurant -> {
          RestaurantDto dto = new RestaurantDto();
          dto.setRestaurantid(restaurant.getId());
          dto.setName(restaurant.getName());
          dto.setImageurl(restaurant.getImageurl());

          return dto;
        })
        .collect(Collectors.toList());

    List<String> enabledRestaurantNames = aRepo.findAll().stream()
        .filter(e -> Boolean.TRUE.equals(e.getEnabled())) // Null-safe comparison
        .map(e -> e.getName())
        .toList();

    System.out.println(enabledRestaurantNames);

    List<RestaurantDto> filteredRestaurants = restaurants.stream()
        .filter(e -> enabledRestaurantNames.contains(e.getName())) // Ensure terminal operation
        .toList();

    System.out.println(restaurants);
    return filteredRestaurants;
  }

  @GetMapping //exist
  public List<RestaurantDto> getRestaurants(@RequestAttribute("userid") String name) {
    User user = restTemplate.getForObject("http://localhost:9091/users/users/" + name, User.class);
    List<RestaurantDto> restaurants = repo.findAll()
        .stream()
        .map(restaurant -> {
          RestaurantDto dto = new RestaurantDto();
          dto.setRestaurantid(restaurant.getId());
          dto.setName(restaurant.getName());
          dto.setImageurl(restaurant.getImageurl());
          dto.setRating(restaurant.getRating());
          dto.setFollowedByCurrentUser(
              fRepo.existsByUserIdAndRestaurantId(user.getId(), restaurant.getId()));
          return dto;
        })
        .collect(Collectors.toList());

    List<String> enabledRestaurantNames = aRepo.findAll().stream()
        .filter(e -> Boolean.TRUE.equals(e.getEnabled())) // Null-safe comparison
        .map(e -> e.getName())
        .toList();

    System.out.println(enabledRestaurantNames);

    List<RestaurantDto> filteredRestaurants = restaurants.stream()
        .filter(e -> enabledRestaurantNames.contains(e.getName())) // Ensure terminal operation
        .toList();

    System.out.println(restaurants);
    return filteredRestaurants;
  }

  @PostMapping("/follow/{restaurantid}")//exist
  public void follow(@RequestAttribute("userid") String name, @PathVariable int restaurantid) {
    User user = restTemplate.getForObject("http://localhost:9091/users/users/" + name, User.class);
    Restaurant restaurant = repo.findById(restaurantid).orElseThrow();
    Followed followed = new Followed().setUser(user).setRestaurant(restaurant);
    fRepo.save(followed);

  }

  @DeleteMapping("/unfollow/{restaurantid}") //exist
  public void unfollow(@RequestAttribute("userid") String name, @PathVariable int restaurantid) {
    // Fetch the user from the external service
    User user = restTemplate.getForObject("http://localhost:9091/users/users/" + name, User.class);

    // Fetch the restaurant
    Restaurant restaurant = repo.findById(restaurantid)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found"));

    // Fetch the Followed relationship
    Followed followed = fRepo.findByUserAndRestaurant(user, restaurant)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Followed entity not found"));

    // Delete the Followed entity
    fRepo.deleteById(followed.getId());
  }

  @GetMapping("/{id}") //exist
  public Restaurant getRestaurant(@PathVariable int id) {
    return repo.findById(id).orElseThrow();
  }

  @GetMapping("/items") //exist
  public List<MenuItems> getItemz(@RequestAttribute("restaurantid") int restaurantid) {
    return restaurantItemRepo.findAll().stream().filter(e -> e.getRestaurant().getId() == restaurantid)
        .map(e -> e.getItem()).toList();
  }

  @GetMapping("/menu/{id}") //exist
  public MenuItems getMenuItems(@PathVariable int id) {
    return mRepo.findById(id).orElseThrow();
  }

  @GetMapping("/getUsers") //exist
  public Iterable<User> getUsers(@RequestAttribute("restaurantid") int restaurantid) {
    return repo.findById(restaurantid).orElseThrow().getUsers();
  }

  @GetMapping("/frenquently/items")//exist
  public Iterable<RestaurantItem> getItems(@RequestAttribute("userid") String userid) {
    User user = restTemplate.getForObject("http://localhost:9091/users/users/" + userid, User.class);
    System.out.println(user);
    List<MenuItems> items = mRepo.findMostFrequentItemsByUser(user.getId());
    return restaurantItemRepo.findAll().stream().filter(e -> items.contains(e.getItem())).toList();
  }

  @GetMapping("/frenquently/itemz") //exist
  public Iterable<RestaurantItem> getItemz(@RequestAttribute("userid") String userid) {
    User user = restTemplate.getForObject("http://localhost:9091/users/users/" + userid, User.class);
    System.out.println(user);
    return restaurantItemRepo.findMostFrequentItemsByUser(user.getId());

  }

  @GetMapping("/{id}/getitems") //exist
  public Iterable<MenuItems> getitems(@PathVariable(name = "id") int restaurantid,
      @RequestAttribute("userid") String userid) {
    User user = restTemplate.getForObject("http://localhost:9091/users/users/" + userid, User.class);
    return mRepo.findMostFrequentItemsByUserAndRestaurant(user.getId(), restaurantid);
  }

  @GetMapping("/{id}/getitemz") //exist
  public Iterable<RestaurantItem> getitemz(@PathVariable(name = "id") int restaurantid,
      @RequestAttribute("userid") String userid) {
    User user = restTemplate.getForObject("http://localhost:9091/users/users/" + userid, User.class);
    return restaurantItemRepo.findMostFrequentItemsByUserAndRestaurant(user.getId(), restaurantid);
  }

  @GetMapping("/{id}/items") //exist 
  public Iterable<RestaurantItem> getRestaurantitems(@PathVariable(name = "id") int restaurantid,
      @RequestAttribute("userid") String userid) {
    User user = restTemplate.getForObject("http://localhost:9091/users/users/" + userid, User.class);
    return restaurantItemRepo.findAll().stream().filter(e -> e.getRestaurant().getId() == restaurantid).toList();
  }


  @PostMapping("/addtomenu") //exist
  public void addToMenu(@RequestAttribute("restaurantid") int restaurantid, @RequestBody Itemrest item) {
    Restaurant restaurant = repo.findById(restaurantid)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
            "Restaurant not found"));

    RestaurantItem restaurantItem = new RestaurantItem();
    restaurantItem.setRestaurant(restaurant); // Associate the restaurant with the item
    MenuItems items = new MenuItems();
    items.setName(item.getItem().getName());
    items.setDescription(item.getItem().getDescription());

    restaurantItem.setItem(items); // Associate the menu item with the restaurant item
    restaurantItem.setPrice(item.getPrice());
    restaurantItemRepo.save(restaurantItem);
    repo.save(restaurant);

  }


  @PostMapping("/addEmployee") //exist
  public ResponseEntity<User> addEmployee(@RequestAttribute("restaurantid") int restaurantid, @RequestBody User user) {
    System.out.println(user);
    Restaurant restaurant = repo.findById(restaurantid).orElseThrow();
    String encryptedPassword = passwordEncoder.encode(user.getPassword());
    user.setPassword(encryptedPassword);
    restaurant.getUsers().add(user);
    repo.save(restaurant);
    return ResponseEntity.status(HttpStatus.CREATED).body(user);
  }

  @DeleteMapping("/{id}") //exist
  public void deleteRestaurant(@PathVariable int id, @RequestAttribute("userid") String name) {
    User user = restTemplate.getForObject("http://localhost:9091/users/users/" + name, User.class);
    if (user.getRole().equals("ADMIN")) {
      repo.deleteById(id);
    }
  }
  @GetMapping("/frequently")
  public Iterable<RestaurantItem> getRestoItemzz() {
    return restaurantItemRepo.findMostFrequentItems();
  }
  @DeleteMapping("/item/{id}")//exist
  public void deleteItem(@PathVariable int id, @RequestAttribute("restaurantid") int restauranid) {
    RestaurantItem item = restaurantItemRepo.findAll().stream()
        .filter(e -> e.getRestaurant().getId() == restauranid && e.getItem().getId() == id).findFirst().orElseThrow();
    restaurantItemRepo.deleteById(item.getId());
  }

  @PutMapping("/uptomenu")
public void upMenu(@RequestAttribute("restaurantid") int restaurantid, @RequestBody Itemrest item) {
    // Find the restaurant
    Restaurant restaurant = repo.findById(restaurantid)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found"));

    // Find the restaurant item
    RestaurantItem restaurantItem = restaurantItemRepo.findAll().stream()
        .filter(e -> e.getRestaurant().getId() == restaurantid && 
                     e.getItem().getName().equals(item.getItem().getName()))
        .findFirst()
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
            "RestaurantItem not found for the given restaurant and item name"));

    // Log the restaurant item
    logger.info("Found RestaurantItem: {}", restaurantItem);

    // Update the menu item details
    MenuItems menuItem = new MenuItems();
    menuItem.setId(restaurantItem.getItem().getId());
    menuItem.setName(item.getItem().getName());
    menuItem.setDescription(item.getItem().getDescription());
    menuItem.setAvailability(restaurantItem.getItem().getAvailability());
    menuItem.setImageurl(restaurantItem.getItem().getImageurl());

    // Associate the updated menu item and price
    restaurantItem.setItem(menuItem);
    restaurantItem.setPrice(item.getPrice());

    // Save the updated restaurant item
    restaurantItemRepo.save(restaurantItem);

    logger.info("Updated RestaurantItem successfully: {}", restaurantItem);
}


}
