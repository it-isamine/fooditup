package com.example.webapp.controller;

import java.util.ArrayList;
import java.util.List;
import javax.naming.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.example.webapp.model.AuthRequest;
import com.example.webapp.model.MenuItems;
import com.example.webapp.model.Order;
import com.example.webapp.model.RegisterForm;
import com.example.webapp.model.RestItem;
import com.example.webapp.model.Restaurant;
import com.example.webapp.model.RestaurantDto;
import com.example.webapp.model.User;
import com.example.webapp.repo.WebAppService;
import com.example.webapp.service.JWTService;
import com.example.webapp.service.SearchService;

import jakarta.servlet.http.HttpServlet;
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
        Iterable<MenuItems> items = webAppService.getItems(token);
        Iterable<RestaurantDto> restaurants = webAppService.getRestaurantDto(token);
        User user = webAppService.getUser(token);

        model.addAttribute("employee", user);
        model.addAttribute("restaurants", restaurants);
        model.addAttribute("itemz", items);
        return "home";
    }

    @PostMapping("/follow/{restaurantid}")
    public ModelAndView follow(@PathVariable int restaurantid, HttpServletRequest request) {
        String token = (String) request.getSession().getAttribute("jwt");
        System.out.println(token);
        webAppService.followRestaurant(token, restaurantid);
        return new ModelAndView("redirect:/home");

    }

    @PostMapping("/unfollow/{restaurantid}")
    public ModelAndView unfollow(@PathVariable int restaurantid, HttpServletRequest request) {
        String token = (String) request.getSession().getAttribute("jwt");
        webAppService.unfollowRestaurant(token, restaurantid);
        return new ModelAndView("redirect:/home");

    }

    @GetMapping("/profile")
    public String getProfile(Model model, HttpServletRequest request) {
        String token = (String) request.getSession().getAttribute("jwt");
        User user = webAppService.getUser(token);
        Iterable<Order> orders = webAppService.getORdersOfUser(token);
        model.addAttribute("user", user);
        model.addAttribute("orders", orders);
        System.out.println(orders);
        return "profile";
    }

    @PostMapping("/addtoInventory")
    public ModelAndView addOrder(@ModelAttribute RestItem menuItem) {
        System.out.println("something" + menuItem);
        List<MenuItems> orders = order.getItems();
        MenuItems item = webAppService.getItem(menuItem.getMenuid());
        Restaurant restaurant = webAppService.gRestaurant(menuItem.getRestaurantid());
        orders.add(item);
        order.setRestaurant(restaurant);
        order.setItems(orders);
        System.out.println(order);

        return new ModelAndView("redirect:/home");
    }

    @GetMapping("/inventory")
    public String inventory(Model model) {
        model.addAttribute("order", order);
        order.getItems().forEach(e -> System.out.println("soemthing" + e));
        return "inventory";

    }

    @PostMapping("/deleteItem")
    public ModelAndView deleteItem(@ModelAttribute MenuItems item) {
        // Defensive copy of the list
        List<MenuItems> items = new ArrayList<>(order.getItems());
        // Ensure proper removal logic
        if (items.remove(item)) {
            System.out.println("Item removed successfully: " + item);
        } else {
            System.out.println("Item not found: " + item);
        }
        order.setItems(items);

        return new ModelAndView("redirect:/inventory");
    }

    @PostMapping("/sendAllItems")
    public ModelAndView sendOrder(HttpServletRequest request) {
        String token = (String) request.getSession().getAttribute("jwt");
        System.out.println("skmethig" + order);
        Order order2 = webAppService.addOrder(token, order);
        System.out.println(order2);
        return new ModelAndView("redirect:/home");

    }

    @GetMapping("/restaurant/{id}")
    public String getResto(Model model, @PathVariable int id, HttpServletRequest request) {
        String token = (String) request.getSession().getAttribute("jwt");
        System.out.println("aezefrr");
        Restaurant restaurant = webAppService.gRestaurant(id);
        Iterable<MenuItems> items = webAppService.getrestoItems(token, id);
        System.out.println("tttttttttttttttttttttt" + restaurant);
        model.addAttribute("restaurant", restaurant);
        model.addAttribute("itemz", items);
        return "restaurant";

    }

    @GetMapping("/log")
    public ModelAndView logout() {
        return new ModelAndView("redirect:http://localhost:9912");
    }

    @GetMapping("/search")
    public String search(@RequestParam("query") String query, Model model) {
        int threshold = 2; // Allow up to 2 typo differences
        List<Restaurant> fuzzyRestaurants = (List<Restaurant>) searchService.searchRestaurantsFuzzy(query, threshold);
        System.out.println(fuzzyRestaurants);
        model.addAttribute("query", query);
        model.addAttribute("restaurants", fuzzyRestaurants);
        return "home";
    }

}
