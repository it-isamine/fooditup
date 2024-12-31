package com.example.restaurant.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import com.example.restaurant.repo.*;
import com.example.restaurant.adminrepo.AdminRepo;
import com.example.restaurant.model.*;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

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
  FollowedRepo fRepo;
  @Autowired
  AdminRepo aRepo;

  @GetMapping("/names")
  public ResponseEntity<Iterable<String>> getRestaurantsNames() {
    return ResponseEntity.status(HttpStatus.OK).body(repo.findAll().stream().map(y -> y.getName().toString()).toList());
  }
  @GetMapping("/adminn")
  public Iterable<com.example.restaurant.adminmodel.Restaurant> gett() {
    return aRepo.findAll();
  }

  @GetMapping("/search/{name}")
  public Restaurant searchResto(@PathVariable String name) {
    return repo.findByName(name).orElseThrow();
  }
  @PostMapping("/visibility/{id}")
  public void isenable(@PathVariable int id) {   
      com.example.restaurant.adminmodel.Restaurant restau = aRepo.findById(id).orElseThrow();
     
     boolean visibility= !restau.getIsEnable();
      restau.setIsEnable(visibility);
      aRepo.save(restau);
    
  }

  @GetMapping("/user-info")
  public String someMethod(@RequestAttribute("userid") String userId,
      @RequestAttribute("restaurantid") String restaurantId) {
    // Now you can use userId and restaurantId
    System.out.println("User ID: " + userId);
    System.out.println("Restaurant ID: " + restaurantId);
    return "Processed";
  }

  @GetMapping("/admin")
  public Iterable<Restaurant> getRestaurantz() {
    return repo.findAll();
  }
  @GetMapping("/getresto")
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
.filter(e -> Boolean.TRUE.equals(e.getIsEnable())) // Null-safe comparison
.map(e -> e.getName())
.toList();

System.out.println(enabledRestaurantNames);

List<RestaurantDto> filteredRestaurants = restaurants.stream()
.filter(e -> enabledRestaurantNames.contains(e.getName())) // Ensure terminal operation
.toList();


System.out.println(restaurants);
return filteredRestaurants;
  }

  @GetMapping
  public List<RestaurantDto> getRestaurants(@RequestAttribute("userid") String name) {
    User user = restTemplate.getForObject("http://localhost:9091/users/users/" + name, User.class);
    List<RestaurantDto> restaurants = repo.findAll()
        .stream()
        .map(restaurant -> {
          RestaurantDto dto = new RestaurantDto();
          dto.setRestaurantid(restaurant.getId());
          dto.setName(restaurant.getName());
          dto.setImageurl(restaurant.getImageurl());
          dto.setFollowedByCurrentUser(
              fRepo.existsByUserIdAndRestaurantId(user.getId(), restaurant.getId()));
          return dto;
        })
        .collect(Collectors.toList());
     
        List<String> enabledRestaurantNames = aRepo.findAll().stream()
    .filter(e -> Boolean.TRUE.equals(e.getIsEnable())) // Null-safe comparison
    .map(e -> e.getName())
    .toList();

System.out.println(enabledRestaurantNames);

List<RestaurantDto> filteredRestaurants = restaurants.stream()
    .filter(e -> enabledRestaurantNames.contains(e.getName())) // Ensure terminal operation
    .toList();


System.out.println(restaurants);
    return filteredRestaurants;
  }

  @PostMapping("/follow/{restaurantid}")
  public void follow(@RequestAttribute("userid") String name, @PathVariable int restaurantid) {
    User user = restTemplate.getForObject("http://localhost:9091/users/users/" + name, User.class);
    Restaurant restaurant = repo.findById(restaurantid).orElseThrow();
    Followed followed = new Followed().setUser(user).setRestaurant(restaurant);
    fRepo.save(followed);

  }

  @DeleteMapping("/unfollow/{restaurantid}")
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

  @GetMapping("/{id}")
  public Restaurant getRestaurant(@PathVariable int id) {
    return repo.findById(id).orElseThrow();
  }

  @GetMapping("/items")
  public List<MenuItems> getItemz(@RequestAttribute("restaurantid") int restaurantid) {
    return restaurantItemRepo.findAll().stream().filter(e -> e.getRestaurant().getId() == restaurantid)
        .map(e -> e.getItem()).toList();
  }

  // @PostMapping("/findByMenu")
  // public ResponseEntity<Restaurant> getRestaurantByMenu(@RequestBody MenuItems
  // menuItems) {
  // // Find the first restaurant that contains the given menu item
  // Optional<Restaurant> restaurant = repo.findAll().stream()
  // .filter(e -> e.getItems().contains(menuItems))
  // .findFirst();

  // // Return the restaurant if found, otherwise return a 404 Not Found
  // return restaurant.map(ResponseEntity::ok)
  // .orElse(ResponseEntity.notFound().build());
  // }
  @GetMapping("/menu/{id}")
  public MenuItems getMenuItems(@PathVariable int id) {
    return mRepo.findById(id).orElseThrow();
  }

  @GetMapping("/getUsers")
  public Iterable<User> getUsers(@RequestAttribute("restaurantid") int restaurantid) {
    return repo.findById(restaurantid).orElseThrow().getUsers();
  }

  @GetMapping("/frenquently/items")
  public Iterable<RestaurantItem> getItems(@RequestAttribute("userid") String userid) {
    User user = restTemplate.getForObject("http://localhost:9091/users/users/" + userid, User.class);
    System.out.println(user);
    List<MenuItems> items = mRepo.findMostFrequentItemsByUser(user.getId());
    return restaurantItemRepo.findAll().stream().filter(e->items.contains(e.getItem())).toList();
  }
  @GetMapping("/frenquently/itemz")
  public Iterable<RestaurantItem> getItemz(@RequestAttribute("userid") String userid) {
    User user = restTemplate.getForObject("http://localhost:9091/users/users/" + userid, User.class);
    System.out.println(user);
    return restaurantItemRepo.findMostFrequentItemsByUser(user.getId());
   
  }

  @GetMapping("/{id}/getitems")
  public Iterable<MenuItems> getitems(@PathVariable(name = "id") int restaurantid,
      @RequestAttribute("userid") String userid) {
    User user = restTemplate.getForObject("http://localhost:9091/users/users/" + userid, User.class);
    return mRepo.findMostFrequentItemsByUserAndRestaurant(user.getId(), restaurantid);
  }
  @GetMapping("/{id}/getitemz")
  public Iterable<RestaurantItem> getitemz(@PathVariable(name = "id") int restaurantid,
      @RequestAttribute("userid") String userid) {
    User user = restTemplate.getForObject("http://localhost:9091/users/users/" + userid, User.class);
    return restaurantItemRepo.findMostFrequentItemsByUserAndRestaurant(user.getId(), restaurantid);
  }
  @GetMapping("/{id}/items")
  public Iterable<RestaurantItem> getRestaurantitems(@PathVariable(name = "id") int restaurantid,
      @RequestAttribute("userid") String userid) {
    User user = restTemplate.getForObject("http://localhost:9091/users/users/" + userid, User.class);
    return restaurantItemRepo.findAll().stream().filter(e->e.getRestaurant().getId()==restaurantid).toList();
  }
  @PostMapping("/save")
  public void saveRestaurant(@RequestBody RestaurantDTO restaurantDTO) {
    System.out.println("dddddddddddddddd" + restaurantDTO);
    // Fetch the restaurant using the restaurantId
    Restaurant restaurant = new Restaurant().setName(restaurantDTO.getName())
        .setRestaurantid(restaurantDTO.getRestaurantid())
        .setrating(restaurantDTO.getRating())
        .setcreated(restaurantDTO.getCreatedAt())
        .setupdated(restaurantDTO.getUpdatedAt())
        .setusers(restaurantDTO.getUsers());

    // Create the RestaurantItem objects
    for (RestaurantItemDTO itemDTO : restaurantDTO.getRestaurantItems()) {
      MenuItems menuItem = itemDTO.getItem();

      RestaurantItem restaurantItem = new RestaurantItem();
      restaurantItem.setRestaurant(restaurant); // Associate the restaurant with the item
      restaurantItem.setItem(menuItem); // Associate the menu item with the restaurant item
      restaurantItem.setPrice(itemDTO.getPrice());

      // Save the restaurant item
      restaurantItemRepo.save(restaurantItem);
    }

    // Save the restaurant entity
    repo.save(restaurant);
  }

  @PostMapping
  public ResponseEntity<Restaurant> addRestaurant(@RequestBody Restaurant resto) {
    // Save the restaurant (the ID will be generated automatically by the database)
    System.out.println(resto);
    repo.save(resto);
    // Return the restaurant object with the associated menu items, indicating
    // successful creation
    return ResponseEntity.status(HttpStatus.CREATED).body(resto);
  }

  // @GetMapping("/{id}/menu")
  // public ResponseEntity<Iterable<MenuItems>> getMenuItems(@PathVariable int id)
  // {
  // return
  // ResponseEntity.status(HttpStatus.OK).body(repo.findById(id).orElseThrow().getItems());
  // }

  // @GetMapping("/{id}/menu/{menuid}")
  // public ResponseEntity<?> getMenu(@PathVariable int id,@PathVariable int
  // menuid) {
  // return
  // ResponseEntity.status(HttpStatus.OK).body(repo.findById(id).orElseThrow().getItems().stream().filter(item->item.getId()==menuid).findFirst());
  // }

  @PostMapping("/addtomenu")
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

  @PostMapping("/addEmployee/{name}/to/{restaurantid}")
  public ResponseEntity<User> addUser(@PathVariable int restaurantid, @PathVariable String name) {
    // Restaurant restaurant = repo.findAll().stream()
    // .filter(resto -> resto.getUsers().stream()
    // .anyMatch(user -> user.getId().equals(userid)))
    // .findFirst()po
    // .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
    // "Restaurant not found"));
    // List<User> users = restaurant.getUsers();
    // users.add(usero);
    // repo.save(restaurant);
    User user = restTemplate.getForObject("http://localhost:9091/users/users/" + name, User.class);
    Restaurant restaurant = repo.findById(restaurantid).orElseThrow();
    restaurant.getUsers().add(user);
    repo.save(restaurant);
    return ResponseEntity.status(HttpStatus.CREATED).body(user);
  }

  @PostMapping("/addEmployee")
  public ResponseEntity<User> addEmployee(@RequestAttribute("restaurantid") int restaurantid, @RequestBody User user) {
    // Restaurant restaurant = repo.findAll().stream()
    // .filter(resto -> resto.getUsers().stream()
    // .anyMatch(user -> user.getId().equals(userid)))
    // .findFirst()po
    // .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
    // "Restaurant not found"));
    // List<User> users = restaurant.getUsers();
    // users.add(usero);

    // repo.save(restaurant);
    System.out.println(user);
    Restaurant restaurant = repo.findById(restaurantid).orElseThrow();
    String encryptedPassword = passwordEncoder.encode(user.getPassword());
    user.setPassword(encryptedPassword);
    restaurant.getUsers().add(user);
    repo.save(restaurant);
    return ResponseEntity.status(HttpStatus.CREATED).body(user);
  }

  @DeleteMapping("/{id}")
  public void deleteRestaurant(@PathVariable int id, @RequestAttribute("userid") String name) {
    User user = restTemplate.getForObject("http://localhost:9091/users/users/" + name, User.class);
    if (user.getRole().equals("ADMIN")) {
      repo.deleteById(id);
    }
  }
  @DeleteMapping("/item/{id}")
  public void deleteItem(@PathVariable int id,@RequestAttribute("restaurantid")int restauranid) {
   RestaurantItem item = restaurantItemRepo.findAll().stream().filter(e->e.getRestaurant().getId()==restauranid && e.getItem().getId()==id).findFirst().orElseThrow();
   restaurantItemRepo.deleteById(item.getId());
  }

}
