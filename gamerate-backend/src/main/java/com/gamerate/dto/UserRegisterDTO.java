package com.gamerate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegisterDTO {

    @NotBlank(message = "username cannot be blank")
    @Size(max = 50, message = "username length cannot exceed 50")
    private String username;

    @NotBlank(message = "password cannot be blank")
    @Size(min = 6, max = 50, message = "password length must be between 6 and 50")
    private String password;

    @Size(max = 50, message = "nickname length cannot exceed 50")
    private String nickname;
}
