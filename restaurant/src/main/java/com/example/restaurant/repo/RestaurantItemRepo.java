package com.example.restaurant.repo;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.restaurant.model.RestaurantItem;

@Repository
public interface RestaurantItemRepo extends JpaRepository<RestaurantItem, Long> {


    @Query(value = """
                SELECT ri.*
                FROM restaurantmenuitems ri
                JOIN ordermenuitems oi ON ri.menuid = oi.menuid
                JOIN orderz o ON oi.orderid = o.orderid
                WHERE o.userid = :userId AND ri.restaurantid = :restaurantId
                GROUP BY ri.menuid
                ORDER BY COUNT(o.orderid) DESC
            """, nativeQuery = true)
    List<RestaurantItem> findMostFrequentItemsByUserAndRestaurant(@Param("userId") UUID userId,
            @Param("restaurantId") int restaurantId);

    @Query(value = """
                SELECT ri.*
                FROM restaurantmenuitems ri
                JOIN ordermenuitems oi ON ri.menuid = oi.menuid
                JOIN orderz o ON oi.orderid = o.orderid
                WHERE o.userid = :userId
                GROUP BY ri.menuid
                ORDER BY COUNT(o.orderid) DESC
            """, nativeQuery = true)
    List<RestaurantItem> findMostFrequentItemsByUser(@Param("userId") UUID userId);

    @Query(value = """
                SELECT ri.*
                FROM restaurantmenuitems ri
                JOIN ordermenuitems oi ON ri.menuid = oi.menuid
                JOIN orderz o ON oi.orderid = o.orderid
                GROUP BY ri.menuid
                ORDER BY COUNT(o.orderid) DESC
            """, nativeQuery = true)
    List<RestaurantItem> findMostFrequentItems();
}
