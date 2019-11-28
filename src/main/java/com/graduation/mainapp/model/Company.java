package com.graduation.mainapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
    @JsonIgnore
    private byte[] logo;

    @JsonIgnore
    @OneToMany
    @JoinColumn(name = "user_id")
    private Set<User> users;

    @ToString.Exclude
    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "company_restaurant",
            joinColumns = {@JoinColumn(name = "company_id")}, inverseJoinColumns = {@JoinColumn(name = "restaurant_id")})
    private Set<Restaurant> restaurants;

    public void deleteUser(User user) {
        user.setCompany(null);
        users.remove(user);
    }
}
