package com.gamerate.vo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class GameRankingVO {

    private Long gameId;

    private String name;

    private String coverUrl;

    private BigDecimal averageScore;

    private Integer ratingCount;

    private Integer favoriteCount;

    private Integer commentCount;

    private Integer viewCount;

    private LocalDate releaseDate;
}
