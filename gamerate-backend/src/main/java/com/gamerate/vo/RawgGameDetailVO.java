package com.gamerate.vo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class RawgGameDetailVO {

    private Long rawgId;

    private String slug;

    private String name;

    private String description;

    private String released;

    private String backgroundImage;

    private String website;

    private BigDecimal rating;

    private Integer metacritic;

    private List<String> developers;

    private List<String> publishers;

    private List<String> platforms;

    private List<String> genres;
}
