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
@EqualsAndHashCode(exclude = {"companies"})
public class Restaurant {
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String address;

    @NotBlank
    private String phoneNumber;

    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    @Lob
    private byte[] logo;

    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "restaurants", fetch = FetchType.LAZY)
    private Set<Company> companies;

    public void addCompany(Company company) {
        companies.add(company);
        company.getRestaurants().add(this);
    }

    public void removeCompany(Company company) {
        companies.remove(company);
        company.getRestaurants().remove(this);
    }

}
