package com.example.order.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class OrderDto {

    private int id;

    private Restaurant restaurant;

    private List<MenuItems> items = new ArrayList<>();

    private LocalDateTime createdAt;

}