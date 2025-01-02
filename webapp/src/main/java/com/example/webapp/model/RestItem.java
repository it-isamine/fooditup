package com.example.webapp.model;

import java.util.List;

import lombok.Data;

@Data
public class RestItem {

    private int restaurantid;
    private int menuid;
    private Double price;
    private List<Integer> sidesid;

}
