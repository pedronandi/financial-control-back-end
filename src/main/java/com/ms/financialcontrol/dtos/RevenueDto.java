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

    @NotBlank(message = "{description.mandatory}")
    private String description;

    @NotNull(message = "{amount.mandatory}")
    @Digits(integer = 6, fraction = 2, message = "{amount.digits-format}")
    @Positive(message = "{amount.positive}")
    private BigDecimal amount;

    @NotNull(message = "{date.mandatory}")
    private LocalDate date;

    @NotNull(message = "{category.id.mandatory}")
    private UUID categoryId;
}
