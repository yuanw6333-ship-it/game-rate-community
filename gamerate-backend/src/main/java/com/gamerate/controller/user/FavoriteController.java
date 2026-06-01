package com.gamerate.controller.user;

import com.gamerate.common.result.PageResult;
import com.gamerate.common.result.Result;
import com.gamerate.service.FavoriteService;
import com.gamerate.vo.FavoriteStatusVO;
import com.gamerate.vo.MyFavoriteVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "游戏收藏接口")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    @Operation(summary = "收藏游戏")
    @PostMapping("/{gameId}")
    public Result<FavoriteStatusVO> favoriteGame(
            @PathVariable @Min(value = 1, message = "gameId must be greater than or equal to 1") Long gameId) {
        return Result.success(favoriteService.favoriteGame(gameId));
    }

    @Operation(summary = "取消收藏")
    @DeleteMapping("/{gameId}")
    public Result<FavoriteStatusVO> unfavoriteGame(
            @PathVariable @Min(value = 1, message = "gameId must be greater than or equal to 1") Long gameId) {
        return Result.success(favoriteService.unfavoriteGame(gameId));
    }

    @Operation(summary = "查询当前用户的游戏收藏状态")
    @GetMapping("/{gameId}/status")
    public Result<FavoriteStatusVO> getFavoriteStatus(
            @PathVariable @Min(value = 1, message = "gameId must be greater than or equal to 1") Long gameId) {
        return Result.success(favoriteService.getFavoriteStatus(gameId));
    }

    @Operation(summary = "分页查询我的收藏列表")
    @GetMapping("/me")
    public Result<PageResult<MyFavoriteVO>> pageMyFavorites(
            @RequestParam(defaultValue = "1")
            @Min(value = 1, message = "pageNum must be greater than or equal to 1") Integer pageNum,
            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "pageSize must be greater than or equal to 1")
            @Max(value = 100, message = "pageSize cannot be greater than 100") Integer pageSize) {
        return Result.success(favoriteService.pageMyFavorites(pageNum, pageSize));
    }
}
