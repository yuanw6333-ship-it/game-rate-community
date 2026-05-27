package com.gamerate.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user")
public class User extends BaseEntity {

    private String username;

    private String passwordHash;

    private String nickname;

    private String avatarUrl;

    private String email;

    private String phone;

    private String bio;

    private Integer status;

    private LocalDateTime lastLoginTime;
}
