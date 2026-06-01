package com.gamerate.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FavoriteStatusVO {

    private Boolean hasFavorited;

    private Long gameId;

    private Long favoriteCount;
}
