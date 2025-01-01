package com.example.restaurant.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "sidemenu")
public class SideMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int sidemenuid;
    int sideid;
    int menuid;

   
}
