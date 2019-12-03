package com.graduation.mainapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String address;

    @NotBlank
    private String phoneNumber;

    @Lob
    private byte[] logo;

    @ToString.Exclude
    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "company_restaurant",
            joinColumns = {@JoinColumn(name = "company_id")}, inverseJoinColumns = {@JoinColumn(name = "restaurant_id")})
    private Set<Restaurant> restaurants;

    public void addRestaurant(Restaurant restaurant) {
        restaurants.add(restaurant);
        restaurant.getCompanies().add(this);
    }

    public void removeRestaurant(Restaurant restaurant) {
        restaurants.remove(restaurant);
        restaurant.getCompanies().remove(this);
    }
}
