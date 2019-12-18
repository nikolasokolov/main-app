package com.graduation.mainapp.repository;

import com.graduation.mainapp.model.MenuItem;
import com.graduation.mainapp.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findAllByRestaurant(Restaurant restaurant);
}
