package com.gamerate.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RatingSubmitDTO {

    @NotNull(message = "gameId cannot be null")
    private Long gameId;

    @NotNull(message = "score cannot be null")
    @DecimalMin(value = "1.0", message = "score cannot be less than 1")
    @DecimalMax(value = "10.0", message = "score cannot be greater than 10")
    private BigDecimal score;
}
