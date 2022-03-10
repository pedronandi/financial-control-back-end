package com.ms.financialcontrol.mappers;

import com.ms.financialcontrol.dtos.RevenueDto;
import com.ms.financialcontrol.models.RevenueModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RevenueMapper {

    @Mapping(source = "categoryId", target = "category.id")
    RevenueModel toModel(RevenueDto revenueDto);
}
