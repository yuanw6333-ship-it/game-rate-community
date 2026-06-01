package com.gamerate.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("game_favorite")
public class GameFavorite extends BaseEntity {

    private Long userId;

    private Long gameId;

    private Integer status;
}
