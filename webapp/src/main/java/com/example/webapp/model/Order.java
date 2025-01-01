package com.example.webapp.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import lombok.Data;

@Data
public class Order {

    private int id;
    private User user;
    private Restaurant restaurant;
    private List<MenuItems> items;
    private LocalDateTime createdAt;
    private String status;

    public Order() {
        this.items = new CopyOnWriteArrayList<>();
    }

}
