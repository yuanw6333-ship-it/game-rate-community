package com.gamerate.vo;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class GameCommentVO {

    private Long id;

    private Long gameId;

    private Long userId;

    private String nickname;

    private String avatarUrl;

    private String content;

    private LocalDateTime commentTime;
}
