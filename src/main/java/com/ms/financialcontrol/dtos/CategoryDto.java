package com.ms.financialcontrol.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CategoryDto {

    @NotBlank
    private String name;
}
