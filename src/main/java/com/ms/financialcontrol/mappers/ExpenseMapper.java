package com.ms.financialcontrol.mappers;

import com.ms.financialcontrol.dtos.ExpenseDto;
import com.ms.financialcontrol.models.ExpenseModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ExpenseMapper {

    @Mapping(source = "categoryId", target = "category.id")
    ExpenseModel toModel(ExpenseDto expenseDto);
}
