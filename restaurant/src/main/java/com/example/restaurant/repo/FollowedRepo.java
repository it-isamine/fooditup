package com.example.restaurant.repo;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.restaurant.model.Followed;
import com.example.restaurant.model.Restaurant;
import com.example.restaurant.model.User;

@Repository
public interface FollowedRepo extends JpaRepository<Followed, Integer> {

    boolean existsByUserIdAndRestaurantId(UUID userId, int restaurantId);

    @Query("SELECT f FROM Followed f WHERE f.user = :user AND f.restaurant = :restaurant")
    Optional<Followed> findByUserAndRestaurant(@Param("user") User user, @Param("restaurant") Restaurant restaurant);

}
