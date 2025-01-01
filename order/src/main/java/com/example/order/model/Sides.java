package com.example.order.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Table(name = "sides")
@Entity
public class Sides {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int sideid;
    String name;
    int price;
}
