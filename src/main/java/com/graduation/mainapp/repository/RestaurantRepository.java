package com.graduation.mainapp.repository;

import com.graduation.mainapp.domain.Restaurant;
import com.graduation.mainapp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    Restaurant findByUser(User user);
}
