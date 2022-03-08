package com.ms.financialcontrol.dtos;

import lombok.Data;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class RevenueDto {

    @NotBlank
    private String description;

    @NotNull
    @Digits(integer = 5, fraction = 2)
    @Positive
    private BigDecimal amount;

    @NotNull
    private LocalDate date;

    @NotNull
    @NumberFormat
    private Long categoryId;
}
