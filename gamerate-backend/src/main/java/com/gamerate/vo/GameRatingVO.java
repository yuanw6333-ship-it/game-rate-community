package com.gamerate.vo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class GameRatingVO {

    private Long gameId;

    private BigDecimal score;

    private LocalDateTime ratingTime;

    private BigDecimal averageScore;

    private Long ratingCount;
}
