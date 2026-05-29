package com.gamerate.controller.admin;

import com.gamerate.common.result.PageResult;
import com.gamerate.common.result.Result;
import com.gamerate.dto.GameCreateDTO;
import com.gamerate.dto.GameQueryDTO;
import com.gamerate.dto.GameUpdateDTO;
import com.gamerate.service.GameService;
import com.gamerate.vo.GameDetailVO;
import com.gamerate.vo.GameListVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "后台游戏管理接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/games")
public class AdminGameController {

    private final GameService gameService;

    @Operation(summary = "后台分页查询游戏")
    @GetMapping
    public Result<PageResult<GameListVO>> pageAdminGames(@Valid GameQueryDTO queryDTO) {
        return Result.success(gameService.pageAdminGames(queryDTO));
    }

    @Operation(summary = "新增游戏")
    @PostMapping
    public Result<GameDetailVO> createGame(@Valid @RequestBody GameCreateDTO createDTO) {
        return Result.success(gameService.createGame(createDTO));
    }

    @Operation(summary = "修改游戏")
    @PutMapping("/{id}")
    public Result<GameDetailVO> updateGame(@PathVariable Long id, @Valid @RequestBody GameUpdateDTO updateDTO) {
        return Result.success(gameService.updateGame(id, updateDTO));
    }

    @Operation(summary = "删除游戏")
    @DeleteMapping("/{id}")
    public Result<Void> deleteGame(@PathVariable Long id) {
        gameService.deleteGame(id);
        return Result.success();
    }
}
