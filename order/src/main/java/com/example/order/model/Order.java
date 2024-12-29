package com.example.order.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
    private int id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "userid", nullable = false)
    private User user;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "restaurantid", nullable = false)
    private Restaurant restaurant;

    @ManyToMany
    @JoinTable(name = "ordermenuitems", joinColumns = @JoinColumn(name = "orderid"), inverseJoinColumns = @JoinColumn(name = "menuid"))
    private List<MenuItems> items = new ArrayList<>();

    @Column(name = "createdat")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    // Override toString to avoid circular references
    @Override
    public String toString() {
        return "Order{id=" + id + ", user=" + (user != null ? user.getId() : "null") +
                ", restaurant=" + (restaurant != null ? restaurant.getId() : "null") +
                ", createdAt=" + createdAt + ", items="
                + (items != null ? "Items list of size: " + items.size() : "No items") + "}";
    }
}
