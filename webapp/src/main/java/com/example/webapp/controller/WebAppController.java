package com.example.webapp.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.webapp.model.MenuItems;
import com.example.webapp.model.Order;
import com.example.webapp.model.OrderDto;
import com.example.webapp.model.RestItem;
import com.example.webapp.model.Restaurant;
import com.example.webapp.model.RestaurantDto;
import com.example.webapp.model.RestaurantItem;
import com.example.webapp.model.Sides;
import com.example.webapp.model.User;
import com.example.webapp.repo.WebAppService;

import com.example.webapp.service.JWTService;
import com.example.webapp.service.SearchService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class WebAppController {

    private static final Logger log = LoggerFactory.getLogger(WebAppController.class);

    @Autowired
    private SearchService searchService;

    @Autowired
    JWTService servicee;

    @Autowired
    WebAppService webAppService;

    @Autowired
    Order order;

    @GetMapping("/home")
    public String home(Model model, @CookieValue(value = "auth_token", defaultValue = "No Auth Token") String token,
            HttpServletRequest request) {

        request.getSession().setAttribute("jwt", token);
        Iterable<RestaurantItem> items = webAppService.getItems(token);
        Iterable<RestaurantDto> restaurants = webAppService.getRestaurantDto(token);
        Iterable<RestaurantItem> frequentlyItems = webAppService.getRestoItemzz();
        Iterable<Sides> sides = webAppService.getSides();
        User user = webAppService.getUser(token);
        model.addAttribute("employee", user);
        model.addAttribute("restaurants", restaurants);
        model.addAttribute("itemz", items);
        model.addAttribute("frItems", frequentlyItems);
        model.addAttribute("sides",sides);

        return "home";
    }

    @PostMapping("/follow/{restaurantid}")
    public ModelAndView follow(@PathVariable int restaurantid, HttpServletRequest request) {
        String token = (String) request.getSession().getAttribute("jwt");
        log.info("token:" + token);
        webAppService.followRestaurant(token, restaurantid);
        return new ModelAndView("redirect:/home");

    }

    @PostMapping("/unfollow/{restaurantid}")
    public ModelAndView unfollow(@PathVariable int restaurantid, HttpServletRequest request) {
        String token = (String) request.getSession().getAttribute("jwt");
        log.info("token:" + token);
        webAppService.unfollowRestaurant(token, restaurantid);
        return new ModelAndView("redirect:/home");

    }

    @GetMapping("/profile/{status}")
    public String getProfile(Model model, HttpServletRequest request, @PathVariable String status) {
        String token = (String) request.getSession().getAttribute("jwt");
        User user = webAppService.getUser(token);
        Iterable<OrderDto> orders = webAppService.getORdersOfUser(token, status);
        model.addAttribute("user", user);
        model.addAttribute("orders", orders);
        log.info("" + orders);
        return "profile";
    }

   @PostMapping("/addtoInventory")
public ModelAndView addOrder(@ModelAttribute RestItem menuItem) {
    log.info("Received MenuItem: {}", menuItem);

    // Retrieve restaurant and item details
    MenuItems item = webAppService.getItem(menuItem.getMenuid());
    Restaurant restaurant = webAppService.gRestaurant(menuItem.getRestaurantid());

    // Fetch and map sides
    List<Integer> sides = Optional.ofNullable(menuItem.getSidesid()).orElse(Collections.emptyList());
    log.info("Selected Sides IDs: {}", sides);

    List<Sides> sidess = sides.stream()
                              .map(webAppService::getSide)
                              .collect(Collectors.toList());
    log.info("Resolved Sides: {}", sidess);

    // Update item with sides
    item.setSides(sidess);

    // Update order
    List<MenuItems> orders = Optional.ofNullable(order.getItems()).orElse(new ArrayList<>());
    orders.add(item);
    order.setItems(orders);
    order.setRestaurant(restaurant);

    log.info("Updated Order: {}", order);

    // Redirect to home
    return new ModelAndView("redirect:/home");
}


    @GetMapping("/inventory")
    public String inventory(Model model) {
        model.addAttribute("order", order);
        order.getItems().forEach(e -> log.info("" + e));
        return "inventory";

    }

    @PostMapping("/deleteItem")
    public ModelAndView deleteItem(@ModelAttribute MenuItems item) {
        List<MenuItems> items = new ArrayList<>(order.getItems());
        if (items.remove(item)) {
            log.info("Item removed successfully: " + item);
        } else {
            log.info("Item not found: " + item);
        }
        order.setItems(items);

        return new ModelAndView("redirect:/inventory");
    }

    @PostMapping("/sendAllItems")
    public ModelAndView sendOrder(HttpServletRequest request) {
        String token = (String) request.getSession().getAttribute("jwt");
        log.info("order" + order);
        Order order2 = webAppService.addOrder(token, order);
        log.info("order update:" + order2);
        return new ModelAndView("redirect:/home");

    }

    @GetMapping("/orders/{id}")
    public String orderDetails(@PathVariable int id, HttpServletRequest request, Model model) {
        String token = (String) request.getSession().getAttribute("jwt");
        Order order = webAppService.getOrder(token, id);
        model.addAttribute("order", order);
        return "view";
    }

    @GetMapping("/restaurant/{id}")
    public String getResto(Model model, @PathVariable int id, HttpServletRequest request) {
        String token = (String) request.getSession().getAttribute("jwt");
        log.info("token:" + token);
        Restaurant restaurant = webAppService.gRestaurant(id);
        Iterable<RestaurantItem> itemz = webAppService.getItemRest(token, id);
        Iterable<RestaurantItem> items = webAppService.getrestoItemz(token, id);
        log.info("restaurant" + restaurant);
        model.addAttribute("restaurant", restaurant);
        model.addAttribute("restaurantItems", items);
        model.addAttribute("itemz", itemz);
        return "restaurant";

    }

    @GetMapping("/edit-profile")
    public String editProfile(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "edit-profile";
    }

    @PostMapping("/profile/update")
    public ModelAndView update(@ModelAttribute User user, HttpServletRequest request) {
        String token = (String) request.getSession().getAttribute("jwt");
        webAppService.updateUser(user, token);
        return new ModelAndView("redirect:/profile");
    }

    @GetMapping("/log")
    public ModelAndView logout() {
        return new ModelAndView("redirect:http://localhost:9912");
    }

    @GetMapping("/search")
    public String search(@RequestParam("query") String query, Model model, HttpServletRequest request) {
        int threshold = 5;
        List<RestaurantDto> fuzzyRestaurants = (List<RestaurantDto>) searchService.searchRestaurantsFuzzy(query,
                threshold);
        log.info("restaurants:"+fuzzyRestaurants);
        model.addAttribute("restaurants", fuzzyRestaurants);
        String token = (String) request.getSession().getAttribute("jwt");
        Iterable<RestaurantItem> items = webAppService.getItems(token);
        User user = webAppService.getUser(token);
        model.addAttribute("employee", user);
        model.addAttribute("itemz", items);
        return "home";
    }

}
