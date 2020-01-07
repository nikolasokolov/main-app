package com.graduation.mainapp.repository.dao.rowmapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvailableRestaurantsRowMapper {
    private Long id;
    private String name;
    private String address;
    private String phoneNumber;
    private byte[] logo;
}
