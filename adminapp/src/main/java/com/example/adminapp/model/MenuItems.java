package com.example.adminapp.model;

import lombok.Data;

@Data
public class MenuItems {

    private int id;
    private String name;
    private String description;
    private Boolean availability;
}
