package com.gamerate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gamerate.common.exception.BusinessException;
import com.gamerate.common.result.PageResult;
import com.gamerate.common.result.ResultCode;
import com.gamerate.dto.CommentSubmitDTO;
import com.gamerate.entity.Game;
import com.gamerate.entity.GameComment;
import com.gamerate.entity.User;
import com.gamerate.mapper.GameCommentMapper;
import com.gamerate.service.CommentService;
import com.gamerate.service.GameService;
import com.gamerate.service.UserService;
import com.gamerate.utils.UserContext;
import com.gamerate.vo.GameCommentVO;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl extends ServiceImpl<GameCommentMapper, GameComment> implements CommentService {

    private static final int STATUS_ENABLED = 1;

    private final GameService gameService;

    private final UserService userService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public GameCommentVO submitComment(CommentSubmitDTO submitDTO) {
        Long userId = UserContext.getUserId();
        Game game = getVisibleGame(submitDTO.getGameId());
        LocalDateTime now = LocalDateTime.now();

        GameComment comment = new GameComment();
        comment.setUserId(userId);
        comment.setGameId(game.getId());
        comment.setContent(submitDTO.getContent().trim());
        comment.setLikeCount(0);
        comment.setStatus(STATUS_ENABLED);
        comment.setIsDeleted(0);
        comment.setCreateTime(now);
        comment.setUpdateTime(now);
        try {
            if (!save(comment)) {
                throw new BusinessException("Failed to save comment");
            }
            refreshGameCommentCount(game.getId());
        } catch (DataAccessException exception) {
            throw new BusinessException("Failed to submit comment");
        }

        return toCommentVO(comment, userService.getById(userId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteComment(Long commentId) {
        Long userId = UserContext.getUserId();
        GameComment comment = getById(commentId);
        if (comment == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "Comment not found");
        }
        if (!userId.equals(comment.getUserId())) {
            throw new BusinessException(ResultCode.FORBIDDEN.getCode(), "Cannot delete another user's comment");
        }
        try {
            if (!removeById(commentId)) {
                throw new BusinessException("Failed to delete comment");
            }
            refreshGameCommentCount(comment.getGameId());
        } catch (DataAccessException exception) {
            throw new BusinessException("Failed to delete comment");
        }
    }

    @Override
    public PageResult<GameCommentVO> pageGameComments(Long gameId, Integer pageNum, Integer pageSize) {
        getVisibleGame(gameId);
        long current = pageNum == null ? 1L : pageNum;
        long size = pageSize == null ? 10L : pageSize;

        Page<GameComment> page = page(
                new Page<>(current, size),
                new LambdaQueryWrapper<GameComment>()
                        .eq(GameComment::getGameId, gameId)
                        .eq(GameComment::getStatus, STATUS_ENABLED)
                        .orderByDesc(GameComment::getCreateTime)
                        .orderByDesc(GameComment::getId)
        );
        Map<Long, User> userMap = buildUserMap(page.getRecords());
        List<GameCommentVO> records = page.getRecords()
                .stream()
                .map(comment -> toCommentVO(comment, userMap.get(comment.getUserId())))
                .toList();
        return PageResult.of(records, page.getTotal(), page.getCurrent(), page.getSize());
    }

    private void refreshGameCommentCount(Long gameId) {
        long commentCount = baseMapper.countActiveCommentsByGameId(gameId);
        boolean updated = gameService.lambdaUpdate()
                .set(Game::getCommentCount, commentCount)
                .set(Game::getUpdateTime, LocalDateTime.now())
                .eq(Game::getId, gameId)
                .update();
        if (!updated) {
            throw new BusinessException("Failed to update game comment count");
        }
    }

    private Game getVisibleGame(Long gameId) {
        Game game = gameService.getById(gameId);
        if (game == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "Game not found");
        }
        if (!Integer.valueOf(STATUS_ENABLED).equals(game.getStatus())) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "Game is not available");
        }
        return game;
    }

    private Map<Long, User> buildUserMap(List<GameComment> comments) {
        Set<Long> userIds = comments.stream()
                .map(GameComment::getUserId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        if (userIds.isEmpty()) {
            return Collections.emptyMap();
        }
        return userService.listByIds(userIds)
                .stream()
                .collect(Collectors.toMap(User::getId, Function.identity(), (left, right) -> left));
    }

    private GameCommentVO toCommentVO(GameComment comment, User user) {
        return GameCommentVO.builder()
                .id(comment.getId())
                .gameId(comment.getGameId())
                .userId(comment.getUserId())
                .nickname(user == null ? null : user.getNickname())
                .avatarUrl(user == null ? null : user.getAvatarUrl())
                .content(comment.getContent())
                .commentTime(comment.getCreateTime())
                .build();
    }
}
