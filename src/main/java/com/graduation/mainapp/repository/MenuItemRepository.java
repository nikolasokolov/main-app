package com.graduation.mainapp.repository;

import com.graduation.mainapp.domain.MenuItem;
import com.graduation.mainapp.domain.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findAllByRestaurant(Restaurant restaurant);

    @Modifying
    @Query(value = "DELETE FROM menu_item mi WHERE mi.restaurant_id = :restaurant_id", nativeQuery = true)
    void deleteAllByRestaurantId(@Param("restaurant_id") Long restaurantId);
}
