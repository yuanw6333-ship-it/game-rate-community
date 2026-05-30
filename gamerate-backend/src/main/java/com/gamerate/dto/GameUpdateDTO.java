package com.gamerate.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class GameUpdateDTO {

    @Size(max = 100, message = "game name length cannot exceed 100")
    private String name;

    @Size(max = 100, message = "original name length cannot exceed 100")
    private String originalName;

    private String description;

    @Size(max = 100, message = "developer length cannot exceed 100")
    private String developer;

    @Size(max = 100, message = "publisher length cannot exceed 100")
    private String publisher;

    private LocalDate releaseDate;

    private Long categoryId;

    private Long platformId;

    @Size(max = 30, message = "sourceType length cannot exceed 30")
    private String sourceType;

    @Size(max = 100, message = "sourceId length cannot exceed 100")
    private String sourceId;

    @Size(max = 500, message = "sourceUrl length cannot exceed 500")
    private String sourceUrl;

    private Long steamAppId;

    @Size(max = 500, message = "coverUrl length cannot exceed 500")
    private String coverUrl;

    @Size(max = 500, message = "backgroundUrl length cannot exceed 500")
    private String backgroundUrl;

    @DecimalMin(value = "0.00", message = "rawgRating cannot be less than 0")
    @DecimalMax(value = "5.00", message = "rawgRating cannot be greater than 5")
    private BigDecimal rawgRating;

    @Max(value = 100, message = "metacriticScore cannot be greater than 100")
    private Integer metacriticScore;

    private Integer status;
}
