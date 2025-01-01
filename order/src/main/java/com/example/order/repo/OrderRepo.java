package com.example.order.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.order.model.Order;

@Repository
public interface OrderRepo extends JpaRepository<Order, Integer> {
    Iterable<Order> findByUser_NameAndStatus(String userName, String status);

    Iterable<Order> findByUser_Name(String userName);

}
