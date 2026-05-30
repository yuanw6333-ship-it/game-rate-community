package com.gamerate.vo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class MyRatingVO {

    private Boolean hasRated;

    private Long gameId;

    private String gameName;

    private String coverUrl;

    private BigDecimal score;

    private LocalDateTime ratingTime;
}
