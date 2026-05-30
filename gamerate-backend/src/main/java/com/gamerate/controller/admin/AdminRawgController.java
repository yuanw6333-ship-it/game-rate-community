package com.gamerate.controller.admin;

import com.gamerate.common.result.Result;
import com.gamerate.dto.RawgImportDTO;
import com.gamerate.dto.RawgSearchDTO;
import com.gamerate.service.RawgService;
import com.gamerate.vo.RawgGameDetailVO;
import com.gamerate.vo.RawgGameSearchVO;
import com.gamerate.vo.RawgImportResultVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "RAWG Import API")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/rawg")
public class AdminRawgController {

    private final RawgService rawgService;

    @Operation(summary = "Search RAWG games")
    @GetMapping("/search")
    public Result<List<RawgGameSearchVO>> searchGames(@Valid RawgSearchDTO searchDTO) {
        return Result.success(rawgService.searchGames(searchDTO));
    }

    @Operation(summary = "Get RAWG game detail")
    @GetMapping("/games/{rawgId}")
    public Result<RawgGameDetailVO> getGameDetail(
            @PathVariable @Min(value = 1, message = "rawgId must be greater than or equal to 1") Long rawgId) {
        return Result.success(rawgService.getGameDetail(rawgId));
    }

    @Operation(summary = "Import RAWG game")
    @PostMapping("/import")
    public Result<RawgImportResultVO> importGame(@Valid @RequestBody RawgImportDTO importDTO) {
        return Result.success(rawgService.importGame(importDTO));
    }
}
