package com.gamerate.vo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class GameListVO {

    private Long id;

    private String name;

    private String originalName;

    private String coverUrl;

    private LocalDate releaseDate;

    private Long categoryId;

    private String categoryName;

    private Long platformId;

    private String platformName;

    private BigDecimal averageScore;

    private Integer ratingCount;

    private Integer viewCount;

    private Integer favoriteCount;

    private Integer commentCount;

    private BigDecimal hotScore;

    private Integer status;
}
