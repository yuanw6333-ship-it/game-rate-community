package com.gamerate.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateDTO {

    @Size(max = 50, message = "nickname length cannot exceed 50")
    private String nickname;

    @Size(max = 500, message = "avatar length cannot exceed 500")
    private String avatar;

    @Email(message = "email format is invalid")
    @Size(max = 100, message = "email length cannot exceed 100")
    private String email;
}
