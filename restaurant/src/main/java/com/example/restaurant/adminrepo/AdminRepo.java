package com.example.restaurant.adminrepo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.restaurant.adminmodel.Restaurant;

@Repository
public interface AdminRepo extends JpaRepository<Restaurant, Integer> {

    Optional<Restaurant> findByName(String name);

    List<Restaurant> findAllByEnabledTrue();
}
