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

    @NotBlank(message = "{revenue.description.mandatory}")
    private String description;

    @NotNull(message = "{revenue.amount.mandatory}")
    @Digits(integer = 6, fraction = 2, message = "{revenue.amount.digits-format}")
    @Positive(message = "{revenue.amount.positive}")
    private BigDecimal amount;

    @NotNull(message = "{revenue.date.mandatory}")
    private LocalDate date;

    @NotNull(message = "{revenue.category-id.mandatory}")
    private UUID categoryId;
}
