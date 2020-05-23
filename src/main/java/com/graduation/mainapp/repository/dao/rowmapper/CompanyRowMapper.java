package com.graduation.mainapp.repository.dao.rowmapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyRowMapper {

    private Long id;
    private String address;
    private byte[] logo;
    private String name;
    private String phoneNumber;
}
