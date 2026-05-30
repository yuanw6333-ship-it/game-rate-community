package com.gamerate.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RawgSearchDTO {

    @NotBlank(message = "keyword cannot be blank")
    private String keyword;

    @Min(value = 1, message = "pageNum must be greater than or equal to 1")
    private Integer pageNum = 1;

    @Min(value = 1, message = "pageSize must be greater than or equal to 1")
    @Max(value = 50, message = "pageSize cannot be greater than 50")
    private Integer pageSize = 10;
}
