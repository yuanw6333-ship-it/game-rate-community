package com.gamerate.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("game_rating")
public class GameRating extends BaseEntity {

    private Long userId;

    private Long gameId;

    private BigDecimal score;

    private String content;

    private Integer status;
}
