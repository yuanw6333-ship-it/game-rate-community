package com.gamerate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gamerate.common.exception.BusinessException;
import com.gamerate.common.result.PageResult;
import com.gamerate.common.result.ResultCode;
import com.gamerate.dto.RatingSubmitDTO;
import com.gamerate.entity.Game;
import com.gamerate.entity.GameRating;
import com.gamerate.mapper.GameRatingMapper;
import com.gamerate.service.GameService;
import com.gamerate.service.RatingService;
import com.gamerate.utils.UserContext;
import com.gamerate.vo.GameRatingVO;
import com.gamerate.vo.MyRatingVO;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
public class RatingServiceImpl extends ServiceImpl<GameRatingMapper, GameRating> implements RatingService {

    private static final int STATUS_ENABLED = 1;

    private final GameService gameService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public GameRatingVO submitRating(RatingSubmitDTO submitDTO) {
        Long userId = UserContext.getUserId();
        Game game = getVisibleGame(submitDTO.getGameId());
        LocalDateTime now = LocalDateTime.now();

        GameRating rating = getByUserAndGame(userId, game.getId());
        try {
            if (rating == null) {
                rating = new GameRating();
                rating.setUserId(userId);
                rating.setGameId(game.getId());
                rating.setScore(submitDTO.getScore());
                rating.setStatus(STATUS_ENABLED);
                rating.setIsDeleted(0);
                rating.setCreateTime(now);
                rating.setUpdateTime(now);
                if (!save(rating)) {
                    throw new BusinessException("Failed to save rating");
                }
            } else {
                rating.setScore(submitDTO.getScore());
                rating.setStatus(STATUS_ENABLED);
                rating.setUpdateTime(now);
                if (!updateById(rating)) {
                    throw new BusinessException("Failed to update rating");
                }
            }
        } catch (DuplicateKeyException exception) {
            throw new BusinessException("Rating already exists, please retry");
        }

        RatingStatistics statistics = refreshGameRatingStatistics(game.getId());
        return GameRatingVO.builder()
                .gameId(game.getId())
                .score(rating.getScore())
                .ratingTime(rating.getUpdateTime())
                .averageScore(statistics.averageScore())
                .ratingCount(statistics.ratingCount())
                .build();
    }

    @Override
    public MyRatingVO getMyRating(Long gameId) {
        Long userId = UserContext.getUserId();
        Game game = getExistingGame(gameId);
        return toMyRatingVO(game, getByUserAndGame(userId, gameId));
    }

    @Override
    public PageResult<MyRatingVO> pageMyRatings(Integer pageNum, Integer pageSize) {
        Long userId = UserContext.getUserId();
        long current = pageNum == null ? 1L : pageNum;
        long size = pageSize == null ? 10L : pageSize;

        Page<GameRating> page = page(
                new Page<>(current, size),
                new LambdaQueryWrapper<GameRating>()
                        .eq(GameRating::getUserId, userId)
                        .eq(GameRating::getStatus, STATUS_ENABLED)
                        .orderByDesc(GameRating::getUpdateTime)
                        .orderByDesc(GameRating::getId)
        );
        Map<Long, Game> gameMap = buildGameMap(page.getRecords());
        List<MyRatingVO> records = page.getRecords()
                .stream()
                .map(rating -> toMyRatingVO(gameMap.get(rating.getGameId()), rating))
                .toList();
        return PageResult.of(records, page.getTotal(), page.getCurrent(), page.getSize());
    }

    private RatingStatistics refreshGameRatingStatistics(Long gameId) {
        long ratingCount = baseMapper.countActiveRatingsByGameId(gameId);
        BigDecimal averageScore = baseMapper.selectAverageScoreByGameId(gameId);
        BigDecimal roundedAverageScore = averageScore == null
                ? BigDecimal.ZERO.setScale(1)
                : averageScore.setScale(1, RoundingMode.HALF_UP);

        boolean updated = gameService.lambdaUpdate()
                .set(Game::getAverageScore, roundedAverageScore)
                .set(Game::getRatingCount, ratingCount)
                .set(Game::getUpdateTime, LocalDateTime.now())
                .eq(Game::getId, gameId)
                .update();
        if (!updated) {
            throw new BusinessException("Failed to update game rating statistics");
        }
        return new RatingStatistics(roundedAverageScore, ratingCount);
    }

    private GameRating getByUserAndGame(Long userId, Long gameId) {
        return getOne(new LambdaQueryWrapper<GameRating>()
                .eq(GameRating::getUserId, userId)
                .eq(GameRating::getGameId, gameId)
                .last("LIMIT 1"));
    }

    private Game getVisibleGame(Long gameId) {
        Game game = getExistingGame(gameId);
        if (!Integer.valueOf(STATUS_ENABLED).equals(game.getStatus())) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "Game is not available");
        }
        return game;
    }

    private Game getExistingGame(Long gameId) {
        Game game = gameService.getById(gameId);
        if (game == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "Game not found");
        }
        return game;
    }

    private Map<Long, Game> buildGameMap(List<GameRating> ratings) {
        Set<Long> gameIds = ratings.stream()
                .map(GameRating::getGameId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        if (gameIds.isEmpty()) {
            return Collections.emptyMap();
        }
        return gameService.listByIds(gameIds)
                .stream()
                .collect(Collectors.toMap(Game::getId, Function.identity(), (left, right) -> left));
    }

    private MyRatingVO toMyRatingVO(Game game, GameRating rating) {
        return MyRatingVO.builder()
                .hasRated(rating != null)
                .gameId(game == null ? (rating == null ? null : rating.getGameId()) : game.getId())
                .gameName(game == null ? null : game.getName())
                .coverUrl(game == null ? null : game.getCoverUrl())
                .score(rating == null ? null : rating.getScore())
                .ratingTime(rating == null ? null : rating.getUpdateTime())
                .build();
    }

    private record RatingStatistics(BigDecimal averageScore, long ratingCount) {
    }
}
