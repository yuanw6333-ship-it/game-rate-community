package com.gamerate.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("game")
public class Game extends BaseEntity {

    private String name;

    private String originalName;

    private String description;

    private String developer;

    private String publisher;

    private LocalDate releaseDate;

    private Long categoryId;

    private Long platformId;

    private String sourceType;

    private String sourceId;

    private String sourceUrl;

    private Long steamAppId;

    private String coverUrl;

    private String backgroundUrl;

    private BigDecimal rawgRating;

    private Integer metacriticScore;

    private LocalDateTime lastSyncTime;

    private BigDecimal averageScore;

    private Integer ratingCount;

    private Integer viewCount;

    private Integer commentCount;

    private Integer favoriteCount;

    private BigDecimal hotScore;

    private Integer status;
}
