package com.gamerate.vo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class RawgGameSearchVO {

    private Long rawgId;

    private String name;

    private String released;

    private String backgroundImage;

    private BigDecimal rating;

    private Integer metacritic;

    private List<String> platforms;

    private List<String> genres;
}
