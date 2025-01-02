package com.example.restaurantadmin.controller;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.restaurantadmin.model.Itemrest;
import com.example.restaurantadmin.model.MenuItems;
import com.example.restaurantadmin.model.Order;
import com.example.restaurantadmin.model.OrderDto;
import com.example.restaurantadmin.model.Restaurant;
import com.example.restaurantadmin.model.User;
import com.example.restaurantadmin.service.WebAppService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/restaurant-admin")
public class RestaurantAdminController {
    Logger logger = (Logger) LoggerFactory.getLogger(RestaurantAdminController.class);
    @Autowired
    WebAppService webAppService;

    @GetMapping("/home")
    public String home(Model model, @CookieValue(value = "auth_token", defaultValue = "No Auth Token") String token,
            HttpServletRequest request) {
        request.getSession().setAttribute("jwt", token);
        Iterable<User> users = webAppService.getUsersOfRestaurant(token);
        Iterable<MenuItems> items = webAppService.getItems(token);

        User user = new User();
        model.addAttribute("employee", user);

        model.addAttribute("itemz", items);
        model.addAttribute("users", users);
        return "home";
    }

    @GetMapping("/menu")
    public String page(Model model, HttpServletRequest request) {
        String token = (String) request.getSession().getAttribute("jwt");
        Iterable<MenuItems> items = webAppService.getItemz(token);
        System.out.println(items);
        model.addAttribute("items", items);
        return "restaurantadminmenu";
    }

    @GetMapping("/orders")
    public String orders(Model model, HttpServletRequest request) {
        String token = (String) request.getSession().getAttribute("jwt");
        Iterable<OrderDto> orders = webAppService.getOrderOfRestaurant(token);
        model.addAttribute("orders", orders);
        return "restaurantadminorders";
    }
    @GetMapping("/orders/{status}")
    public String orders(Model model, HttpServletRequest request,@PathVariable String status) {
        String token = (String) request.getSession().getAttribute("jwt");
        Iterable<OrderDto> orders = webAppService.getOrderOfRestaurantByType(token,status);
        model.addAttribute("orders", orders);
        return "restaurantadminorders";
    }
    @GetMapping("/orders/update/{orderid}")
    public ModelAndView updateOrder(@PathVariable("orderid") int id,
                                    @RequestParam(name = "status") String status,
                                    HttpServletRequest request,
                                    Model model) {
        String token = (String) request.getSession().getAttribute("jwt");
        webAppService.updateOrder(token, status, id);
        Iterable<OrderDto> orders = webAppService.getOrderOfRestaurantByType(token, status);
        model.addAttribute("orders", orders);
        return new ModelAndView("redirect:/restaurant-admin/orders");
    }
    
    
    
    @PostMapping("/addEmployee")
    public ModelAndView addEmployee(@ModelAttribute User user, HttpServletRequest request) {
        String token = (String) request.getSession().getAttribute("jwt");
        webAppService.addEmployee(token, user);
        return new ModelAndView("redirect:/restaurant-admin/home");
    }

    @PostMapping("/menu/add")
    public ModelAndView addMenu(@ModelAttribute Itemrest itemz, HttpServletRequest request) {
        String token = (String) request.getSession().getAttribute("jwt");
        System.out.println(itemz);
        webAppService.addItem(token, itemz);
        return new ModelAndView("redirect:/restaurant-admin/home");
    }

    @GetMapping("/menu/delete/{id}")
    public ModelAndView deleteMenu(@PathVariable int id,HttpServletRequest request) {
        String token = (String) request.getSession().getAttribute("jwt");
        webAppService.deleteItem(token, id);
        return new ModelAndView("redirect:/restaurant-admin/menu");

    }
    @PostMapping("/menu/edit")
    public ModelAndView editMenu(@ModelAttribute("menuItem") Itemrest item,HttpServletRequest request) {
        logger.info(""+item);
        webAppService.updateItem( (String) request.getSession().getAttribute("jwt"), item);
        
        return new ModelAndView("redirect:/restaurant-admin/menu");
    }
    @PostMapping("/users/delete/{id}")
    public ModelAndView deleteuser(@PathVariable UUID id, HttpServletRequest request) throws InterruptedException {
        String token = (String) request.getSession().getAttribute("jwt");
        webAppService.deleteUser(id, token);

        return new ModelAndView("redirect:/restaurant-admin/home");

    }

    @PostMapping("/users/edit/{id}")
    public ModelAndView editUser(@ModelAttribute User user, HttpServletRequest request) {
        String token = (String) request.getSession().getAttribute("jwt");
        webAppService.updateUser(user, token);
        return new ModelAndView("redirect:/restaurant-admin/home");
    }

    @GetMapping("/log")
    public ModelAndView logout() {
        return new ModelAndView("redirect:http://localhost:9912");
    }
}