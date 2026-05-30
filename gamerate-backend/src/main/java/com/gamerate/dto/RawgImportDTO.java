package com.gamerate.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class RawgImportDTO {

    @NotNull(message = "rawgId cannot be null")
    private Long rawgId;

    @NotNull(message = "categoryId cannot be null")
    private Long categoryId;

    @NotNull(message = "platformId cannot be null")
    private Long platformId;

    @Min(value = 0, message = "status must be 0 or 1")
    @Max(value = 1, message = "status must be 0 or 1")
    private Integer status;

    private Boolean overwrite = false;
}
