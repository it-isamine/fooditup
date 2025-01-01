package com.example.webapp.model;

import java.util.List;

import lombok.Data;

@Data
public class MenuItems {

    private int id;
    private String name;
    private String description;
    private Boolean availability;
    private String imageurl;
    private List<Sides> sides;
    
}
