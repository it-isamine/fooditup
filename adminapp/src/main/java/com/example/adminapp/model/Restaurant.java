package com.example.adminapp.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class Restaurant {

    private int restaurantid;
    private String name;
    private Boolean enabled;
}
