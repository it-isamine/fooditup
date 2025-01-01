package com.example.webapp.model;



import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import lombok.Data;

@Data
public class OrderDto {

    private int id;
    private User user;
    private Restaurant restaurant;
    private List<SideMenu> items;
    private LocalDateTime createdAt;
    private String status;

    public OrderDto() {
        this.items = new CopyOnWriteArrayList<>();
    }

}
