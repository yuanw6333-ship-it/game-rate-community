package com.gamerate.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("game_comment")
public class GameComment extends BaseEntity {

    private Long userId;

    private Long gameId;

    private String content;

    private Integer likeCount;

    private Integer status;
}
