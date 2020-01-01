package com.graduation.mainapp.repository;

import com.graduation.mainapp.domain.MenuItem;
import com.graduation.mainapp.domain.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findAllByRestaurant(Restaurant restaurant);
}
