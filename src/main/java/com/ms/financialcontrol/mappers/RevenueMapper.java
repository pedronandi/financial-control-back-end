package com.ms.financialcontrol.mappers;

import com.ms.financialcontrol.dtos.RevenueDto;
import com.ms.financialcontrol.models.RevenueModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface RevenueMapper {

    @Mapping(target = "category.id", source = "categoryId")
    @Mapping(target = "user.id", source = "userId")
    RevenueModel toModel(RevenueDto revenueDto);
}
