package com.example.order.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "menuitems")
public class MenuItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menuid")
    int id;

    String name;

    String description;

    Boolean availability;


  @ManyToMany
    @JoinTable(
      name = "sidemenu", 
      joinColumns = @JoinColumn(name = "menuid"), 
      inverseJoinColumns = @JoinColumn(name = "sideid"))
    List<Sides> sides;

    // Override toString to avoid circular references
    @Override
    public String toString() {
        return "MenuItems{id=" + id + ", name='" + name + "', description='" + description + "'}";
    }
}
