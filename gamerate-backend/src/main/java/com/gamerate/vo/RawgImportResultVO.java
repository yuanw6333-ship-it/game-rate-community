package com.gamerate.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RawgImportResultVO {

    private Long gameId;

    private Boolean imported;

    private Boolean existed;

    private String message;
}
