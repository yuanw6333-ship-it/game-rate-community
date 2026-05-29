package com.gamerate.controller.user;

import com.gamerate.common.result.PageResult;
import com.gamerate.common.result.Result;
import com.gamerate.dto.GameQueryDTO;
import com.gamerate.service.GameService;
import com.gamerate.vo.GameDetailVO;
import com.gamerate.vo.GameListVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "游戏接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/games")
public class GameController {

    private final GameService gameService;

    @Operation(summary = "游戏分页列表")
    @GetMapping
    public Result<PageResult<GameListVO>> pageGames(@Valid GameQueryDTO queryDTO) {
        return Result.success(gameService.pageVisibleGames(queryDTO));
    }

    @Operation(summary = "游戏详情")
    @GetMapping("/{id}")
    public Result<GameDetailVO> getGameDetail(@PathVariable Long id) {
        return Result.success(gameService.getVisibleGameDetail(id));
    }
}
