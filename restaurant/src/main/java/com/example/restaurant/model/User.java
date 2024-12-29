package com.example.restaurant.model;

import java.time.LocalDateTime;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "userid")
    UUID id;

    String name;

    String email;

    String password;

    String phone;

    String address;

    String role;

    LocalDateTime createdat;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "restaurantid")
    @JsonIgnore
    Restaurant restaurant;

    @PrePersist
    public void prePersist() {
        if (id == null) {
            id = UUID.randomUUID(); // Automatically generate the UUID if it's not set
        }
    }
}