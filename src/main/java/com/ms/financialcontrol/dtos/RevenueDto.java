package com.ms.financialcontrol.dtos;

import lombok.Data;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class RevenueDto {

    @NotBlank(message = "Description is mandatory")
    private String description;

    @NotNull(message = "Amount is mandatory")
    @Digits(integer = 6, fraction = 2, message = "Amount must have, at max, {integer} integers and {fraction} decimals")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;

    @NotNull(message = "Date is mandatory and must have to follow UTC format")
    private LocalDate date;

    @NotNull(message = "Category id is mandatory")
    private UUID categoryId;
}
