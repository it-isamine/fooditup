package com.example.restaurant.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "sides")
public class Sides {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int sideid;

    String name;

    int price;

    @Column(name = "image_url")
    String imageurl;

     @ManyToMany
    @JoinTable(name = "sidemenu", joinColumns = @JoinColumn(name = "sideid"), inverseJoinColumns = @JoinColumn(name = "menuid"))
    @JsonIgnore
    List<MenuItems> items;
    
}
