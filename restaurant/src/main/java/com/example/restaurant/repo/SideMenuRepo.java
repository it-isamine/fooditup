package com.example.restaurant.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.restaurant.model.SideMenu;

@Repository
public interface SideMenuRepo extends JpaRepository<SideMenu, Integer> {
    
}
