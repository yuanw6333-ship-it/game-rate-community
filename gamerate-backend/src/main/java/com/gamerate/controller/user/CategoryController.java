package com.gamerate.controller.user;

import com.gamerate.common.result.Result;
import com.gamerate.service.CategoryService;
import com.gamerate.vo.CategoryVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "分类接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "查询启用分类列表")
    @GetMapping
    public Result<List<CategoryVO>> listEnabledCategories() {
        return Result.success(categoryService.listEnabledCategories());
    }
}
