package com.gamerate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentSubmitDTO {

    @NotNull(message = "gameId cannot be null")
    private Long gameId;

    @NotBlank(message = "comment content cannot be blank")
    @Size(max = 500, message = "comment content length cannot exceed 500")
    private String content;
}
