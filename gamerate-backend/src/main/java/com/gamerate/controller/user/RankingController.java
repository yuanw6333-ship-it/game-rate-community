package com.gamerate.controller.user;

import com.gamerate.common.result.PageResult;
import com.gamerate.common.result.Result;
import com.gamerate.service.RankingService;
import com.gamerate.vo.GameRankingVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "游戏排行榜接口")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rankings")
public class RankingController {

    private final RankingService rankingService;

    @Operation(summary = "高分游戏排行榜")
    @GetMapping("/top-score")
    public Result<PageResult<GameRankingVO>> pageTopScoreGames(
            @RequestParam(defaultValue = "1")
            @Min(value = 1, message = "pageNum must be greater than or equal to 1") Integer pageNum,
            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "pageSize must be greater than or equal to 1")
            @Max(value = 100, message = "pageSize cannot be greater than 100") Integer pageSize) {
        return Result.success(rankingService.pageTopScoreGames(pageNum, pageSize));
    }

    @Operation(summary = "热门游戏排行榜")
    @GetMapping("/popular")
    public Result<PageResult<GameRankingVO>> pagePopularGames(
            @RequestParam(defaultValue = "1")
            @Min(value = 1, message = "pageNum must be greater than or equal to 1") Integer pageNum,
            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "pageSize must be greater than or equal to 1")
            @Max(value = 100, message = "pageSize cannot be greater than 100") Integer pageSize) {
        return Result.success(rankingService.pagePopularGames(pageNum, pageSize));
    }

    @Operation(summary = "收藏游戏排行榜")
    @GetMapping("/favorite")
    public Result<PageResult<GameRankingVO>> pageFavoriteGames(
            @RequestParam(defaultValue = "1")
            @Min(value = 1, message = "pageNum must be greater than or equal to 1") Integer pageNum,
            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "pageSize must be greater than or equal to 1")
            @Max(value = 100, message = "pageSize cannot be greater than 100") Integer pageSize) {
        return Result.success(rankingService.pageFavoriteGames(pageNum, pageSize));
    }

    @Operation(summary = "评论游戏排行榜")
    @GetMapping("/comment")
    public Result<PageResult<GameRankingVO>> pageCommentGames(
            @RequestParam(defaultValue = "1")
            @Min(value = 1, message = "pageNum must be greater than or equal to 1") Integer pageNum,
            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "pageSize must be greater than or equal to 1")
            @Max(value = 100, message = "pageSize cannot be greater than 100") Integer pageSize) {
        return Result.success(rankingService.pageCommentGames(pageNum, pageSize));
    }

    @Operation(summary = "新游排行榜")
    @GetMapping("/new")
    public Result<PageResult<GameRankingVO>> pageNewGames(
            @RequestParam(defaultValue = "1")
            @Min(value = 1, message = "pageNum must be greater than or equal to 1") Integer pageNum,
            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "pageSize must be greater than or equal to 1")
            @Max(value = 100, message = "pageSize cannot be greater than 100") Integer pageSize) {
        return Result.success(rankingService.pageNewGames(pageNum, pageSize));
    }
}
