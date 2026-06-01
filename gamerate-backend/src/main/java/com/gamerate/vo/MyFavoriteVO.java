package com.gamerate.vo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class MyFavoriteVO {

    private Long gameId;

    private String gameName;

    private String coverUrl;

    private String description;

    private BigDecimal averageScore;

    private LocalDateTime favoriteTime;
}
