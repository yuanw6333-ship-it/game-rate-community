package com.gamerate.controller.user;

import com.gamerate.common.result.PageResult;
import com.gamerate.common.result.Result;
import com.gamerate.dto.RatingSubmitDTO;
import com.gamerate.service.RatingService;
import com.gamerate.vo.GameRatingVO;
import com.gamerate.vo.MyRatingVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "游戏评分接口")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ratings")
public class RatingController {

    private final RatingService ratingService;

    @Operation(summary = "提交或修改游戏评分")
    @PostMapping
    public Result<GameRatingVO> submitRating(@Valid @RequestBody RatingSubmitDTO submitDTO) {
        return Result.success(ratingService.submitRating(submitDTO));
    }

    @Operation(summary = "查询当前用户对指定游戏的评分")
    @GetMapping("/game/{gameId}/me")
    public Result<MyRatingVO> getMyRating(
            @PathVariable @Min(value = 1, message = "gameId must be greater than or equal to 1") Long gameId) {
        return Result.success(ratingService.getMyRating(gameId));
    }

    @Operation(summary = "分页查询我的评分记录")
    @GetMapping("/me")
    public Result<PageResult<MyRatingVO>> pageMyRatings(
            @RequestParam(defaultValue = "1")
            @Min(value = 1, message = "pageNum must be greater than or equal to 1") Integer pageNum,
            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "pageSize must be greater than or equal to 1")
            @Max(value = 100, message = "pageSize cannot be greater than 100") Integer pageSize) {
        return Result.success(ratingService.pageMyRatings(pageNum, pageSize));
    }
}
