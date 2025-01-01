package com.example.webapp.model;

import java.util.List;

import lombok.Data;

@Data
public class RestItem {

    int restaurantid;
    int menuid;
    Double price;
    List<Integer> sidesid;

}
