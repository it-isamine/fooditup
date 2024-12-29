package com.example.restaurant.repo;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.restaurant.model.MenuItems;

@Repository
public interface MenuRepo extends JpaRepository<MenuItems, Integer> {

    @Query(value = """
                SELECT mi.*
                FROM menuitems mi
                JOIN restaurantmenuitems rmi ON mi.menuid = rmi.menuid
                JOIN ordermenuitems oi ON mi.menuid = oi.menuid
                JOIN orderz o ON oi.orderid = o.orderid
                WHERE o.userid = :userId AND rmi.restaurantid = :restaurantId
                GROUP BY mi.menuid
                ORDER BY COUNT(o.orderid) DESC
            """, nativeQuery = true)
    List<MenuItems> findMostFrequentItemsByUserAndRestaurant(@Param("userId") UUID userId,
            @Param("restaurantId") int restaurantId);

    @Query(value = """
                SELECT mi.*
                FROM menuitems mi
                JOIN ordermenuitems oi ON mi.menuid = oi.menuid
                JOIN orderz o ON oi.orderid = o.orderid
                WHERE o.userid = :userId
                GROUP BY mi.menuid
                ORDER BY COUNT(o.orderid) DESC
            """, nativeQuery = true)
    List<MenuItems> findMostFrequentItemsByUser(@Param("userId") UUID userId);
}
