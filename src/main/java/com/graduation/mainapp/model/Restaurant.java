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
    @JsonIgnore
    private byte[] logo;

    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "restaurants")
    private Set<Company> companies;

}
