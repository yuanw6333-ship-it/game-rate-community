package com.gamerate.controller.user;

import com.gamerate.common.result.PageResult;
import com.gamerate.common.result.Result;
import com.gamerate.dto.CommentSubmitDTO;
import com.gamerate.service.CommentService;
import com.gamerate.vo.GameCommentVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "游戏评论接口")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "提交游戏评论")
    @PostMapping
    public Result<GameCommentVO> submitComment(@Valid @RequestBody CommentSubmitDTO submitDTO) {
        return Result.success(commentService.submitComment(submitDTO));
    }

    @Operation(summary = "删除自己的游戏评论")
    @DeleteMapping("/{commentId}")
    public Result<Void> deleteComment(
            @PathVariable @Min(value = 1, message = "commentId must be greater than or equal to 1") Long commentId) {
        commentService.deleteComment(commentId);
        return Result.success();
    }

    @Operation(summary = "分页查询游戏评论")
    @GetMapping("/game/{gameId}")
    public Result<PageResult<GameCommentVO>> pageGameComments(
            @PathVariable @Min(value = 1, message = "gameId must be greater than or equal to 1") Long gameId,
            @RequestParam(defaultValue = "1")
            @Min(value = 1, message = "pageNum must be greater than or equal to 1") Integer pageNum,
            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "pageSize must be greater than or equal to 1")
            @Max(value = 100, message = "pageSize cannot be greater than 100") Integer pageSize) {
        return Result.success(commentService.pageGameComments(gameId, pageNum, pageSize));
    }
}
