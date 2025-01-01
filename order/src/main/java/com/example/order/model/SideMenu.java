package com.example.order.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "sidemenu")
public class SideMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int sidemenuid;

     @ManyToOne
    @JoinColumn(name = "menuid")
    MenuItems item;

    @ManyToOne
    @JoinColumn(name = "sideid")
    Sides side;

    @ManyToMany(mappedBy = "items")
    @JsonIgnore
    List<Order> orders;
    
}
