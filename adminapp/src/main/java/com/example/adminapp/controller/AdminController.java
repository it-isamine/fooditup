package com.example.adminapp.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.naming.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.example.adminapp.config.SpringSecurityConfig;
import com.example.adminapp.model.AuthRequest;
import com.example.adminapp.model.MenuItems;
import com.example.adminapp.model.Restaurant;
import com.example.adminapp.model.User;
import com.example.adminapp.repo.WebAppService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private static final Logger log = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    WebAppService webAppService;

    @GetMapping("/debug")
    @ResponseBody
    public String debug(Authentication authentication) {
        return "Roles: " + authentication.getAuthorities();
    }

    @GetMapping("/log")
    public ModelAndView logout() {
        return new ModelAndView("redirect:http://localhost:9912");
    }

    @GetMapping("/home")
    public String home(Model model, @CookieValue(value = "auth_token", defaultValue = "No Auth Token") String token,
            HttpServletRequest request) {

        request.getSession().setAttribute("jwt", token);

        User user = webAppService.getUser(token);

        model.addAttribute("user", user);

        return "home";
    }

    @GetMapping("/restaurants")
    public String getRestaurants(Model model) {
        Iterable<Restaurant> restaurants = webAppService.getRestaurant();
        model.addAttribute("restaurants", restaurants);
        return "restaurant-management";
    }

    @PostMapping("/restaurants/delete/{id}")
    public ModelAndView deleteRestaurants(@PathVariable int id, HttpServletRequest request) {
        String token = (String) request.getSession().getAttribute("jwt");
        webAppService.deleteRestaurant(id, token);
        return new ModelAndView("redirect:/admin/restaurants");
    }

    @GetMapping("/users")
    public String getUsers(Model model) {
        Iterable<User> users = webAppService.getUsers();
        model.addAttribute("users", users);
        return "user-management";
    }

    @PostMapping("/users/delete/{id}")
    public ModelAndView deleteuser(@PathVariable UUID id, HttpServletRequest request) throws InterruptedException {
        String token = (String) request.getSession().getAttribute("jwt");
        webAppService.deleteUser(id, token);

        return new ModelAndView("redirect:/admin/users");

    }

    @PostMapping("/users/edit/{id}")
    public ModelAndView editUser(@ModelAttribute User user, HttpServletRequest request) {
        String token = (String) request.getSession().getAttribute("jwt");
        webAppService.updateUser(user, token);
        return new ModelAndView("redirect:/admin/users");
    }
    @PostMapping("/restaurants/disable/{id}")
    public ModelAndView disable(@PathVariable int id) {
    
        webAppService.setVisibility( id);
        return new ModelAndView("redirect:/admin/restaurants");
    }

}
