package com.example.restaurant.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.restaurant.model.RestaurantItem;

@Repository
public interface RestaurantItemRepo extends JpaRepository<RestaurantItem, Integer> {

}
