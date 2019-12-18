package com.graduation.mainapp.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuItemsDTO {
    private Long id;
    private String type;
    private String name;
    private Integer price;
}
