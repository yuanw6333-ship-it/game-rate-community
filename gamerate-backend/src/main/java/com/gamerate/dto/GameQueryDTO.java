package com.gamerate.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class GameQueryDTO {

    @Size(max = 100, message = "keyword length cannot exceed 100")
    private String keyword;

    private Long categoryId;

    private Long platformId;

    private String sort = "default";

    @Min(value = 1, message = "pageNum must be greater than 0")
    private Long pageNum = 1L;

    @Min(value = 1, message = "pageSize must be greater than 0")
    @Max(value = 100, message = "pageSize cannot exceed 100")
    private Long pageSize = 10L;
}
