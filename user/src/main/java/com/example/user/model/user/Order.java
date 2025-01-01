package com.example.user.model.user;

import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "orderz")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderid")
    int id;
    
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "userid")
    User user;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "restaurantid")
    Restaurant restaurant;

    LocalDateTime createdat;

    @PrePersist
    public void prePersist() {
        if (restaurant == null) {
            restaurant.setId(2);
            restaurant.setName("something");
        }
    }
}
