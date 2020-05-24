package com.graduation.mainapp.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyDTO {

    private Long id;
    private String name;
    private String address;
    private String phoneNumber;
    private byte[] logo;
}
