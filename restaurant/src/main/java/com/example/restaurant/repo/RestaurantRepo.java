package com.example.restaurant.repo;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.restaurant.model.*;

@Repository
public interface RestaurantRepo extends JpaRepository<Restaurant, Integer> {

    Optional<Restaurant> findByName(String name);

}
