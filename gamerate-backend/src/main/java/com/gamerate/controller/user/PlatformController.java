package com.gamerate.controller.user;

import com.gamerate.common.result.Result;
import com.gamerate.service.PlatformService;
import com.gamerate.vo.PlatformVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "平台接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/platforms")
public class PlatformController {

    private final PlatformService platformService;

    @Operation(summary = "查询启用平台列表")
    @GetMapping
    public Result<List<PlatformVO>> listEnabledPlatforms() {
        return Result.success(platformService.listEnabledPlatforms());
    }
}
