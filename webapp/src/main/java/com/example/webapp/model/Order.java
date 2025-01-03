package com.example.webapp.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class Order {

    private int id;
    private User user;
    private Restaurant restaurant;
    private List<MenuItems> items;
    private LocalDateTime createdAt;
    
   public Order() {
    this.items = new ArrayList<>();
   }

  
}
