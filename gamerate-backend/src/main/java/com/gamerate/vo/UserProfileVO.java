package com.gamerate.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfileVO {

    private Long id;

    private String username;

    private String nickname;

    private String avatarUrl;

    private String email;

    private String bio;

    private Integer status;
}
