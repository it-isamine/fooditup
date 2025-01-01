package com.example.restaurant.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.restaurant.model.Sides;

@Repository
public interface SideRepo extends JpaRepository<Sides, Integer> {
    
}
