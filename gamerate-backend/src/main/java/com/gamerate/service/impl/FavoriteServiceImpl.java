package com.gamerate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gamerate.common.exception.BusinessException;
import com.gamerate.common.result.PageResult;
import com.gamerate.common.result.ResultCode;
import com.gamerate.entity.Game;
import com.gamerate.entity.GameFavorite;
import com.gamerate.mapper.GameFavoriteMapper;
import com.gamerate.service.FavoriteService;
import com.gamerate.service.GameService;
import com.gamerate.utils.UserContext;
import com.gamerate.vo.FavoriteStatusVO;
import com.gamerate.vo.MyFavoriteVO;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
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
public class FavoriteServiceImpl extends ServiceImpl<GameFavoriteMapper, GameFavorite> implements FavoriteService {

    private static final int STATUS_DISABLED = 0;

    private static final int STATUS_ENABLED = 1;

    private final GameService gameService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FavoriteStatusVO favoriteGame(Long gameId) {
        Long userId = UserContext.getUserId();
        Game game = getVisibleGame(gameId);
        GameFavorite favorite = getByUserAndGame(userId, gameId);
        try {
            if (favorite == null) {
                favorite = new GameFavorite();
                favorite.setUserId(userId);
                favorite.setGameId(gameId);
                favorite.setStatus(STATUS_ENABLED);
                favorite.setIsDeleted(0);
                if (!save(favorite)) {
                    throw new BusinessException("Failed to favorite game");
                }
            } else if (!Integer.valueOf(STATUS_ENABLED).equals(favorite.getStatus())) {
                favorite.setStatus(STATUS_ENABLED);
                favorite.setCreateTime(LocalDateTime.now());
                favorite.setUpdateTime(LocalDateTime.now());
                if (!updateById(favorite)) {
                    throw new BusinessException("Failed to favorite game");
                }
            }
            return buildStatusVO(game.getId(), true, refreshGameFavoriteCount(game.getId()));
        } catch (DuplicateKeyException exception) {
            throw new BusinessException("Game has already been favorited, please retry");
        } catch (DataAccessException exception) {
            throw new BusinessException("Failed to favorite game");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FavoriteStatusVO unfavoriteGame(Long gameId) {
        Long userId = UserContext.getUserId();
        Game game = getExistingGame(gameId);
        GameFavorite favorite = getByUserAndGame(userId, gameId);
        try {
            if (favorite != null && Integer.valueOf(STATUS_ENABLED).equals(favorite.getStatus())) {
                favorite.setStatus(STATUS_DISABLED);
                favorite.setUpdateTime(LocalDateTime.now());
                if (!updateById(favorite)) {
                    throw new BusinessException("Failed to unfavorite game");
                }
            }
            return buildStatusVO(game.getId(), false, refreshGameFavoriteCount(game.getId()));
        } catch (DataAccessException exception) {
            throw new BusinessException("Failed to unfavorite game");
        }
    }

    @Override
    public FavoriteStatusVO getFavoriteStatus(Long gameId) {
        Long userId = UserContext.getUserId();
        Game game = getExistingGame(gameId);
        GameFavorite favorite = getByUserAndGame(userId, gameId);
        boolean hasFavorited = favorite != null && Integer.valueOf(STATUS_ENABLED).equals(favorite.getStatus());
        return buildStatusVO(game.getId(), hasFavorited, baseMapper.countActiveFavoritesByGameId(gameId));
    }

    @Override
    public PageResult<MyFavoriteVO> pageMyFavorites(Integer pageNum, Integer pageSize) {
        Long userId = UserContext.getUserId();
        long current = pageNum == null ? 1L : pageNum;
        long size = pageSize == null ? 10L : pageSize;
        IPage<GameFavorite> page = baseMapper.selectMyFavoritePage(new Page<>(current, size), userId);

        Map<Long, Game> gameMap = buildGameMap(page.getRecords());
        List<MyFavoriteVO> records = page.getRecords()
                .stream()
                .map(favorite -> toMyFavoriteVO(favorite, gameMap.get(favorite.getGameId())))
                .toList();
        return PageResult.of(records, page.getTotal(), page.getCurrent(), page.getSize());
    }

    private long refreshGameFavoriteCount(Long gameId) {
        long favoriteCount = baseMapper.countActiveFavoritesByGameId(gameId);
        boolean updated = gameService.lambdaUpdate()
                .set(Game::getFavoriteCount, favoriteCount)
                .set(Game::getUpdateTime, LocalDateTime.now())
                .eq(Game::getId, gameId)
                .update();
        if (!updated) {
            throw new BusinessException("Failed to update game favorite count");
        }
        return favoriteCount;
    }

    private GameFavorite getByUserAndGame(Long userId, Long gameId) {
        return getOne(new LambdaQueryWrapper<GameFavorite>()
                .eq(GameFavorite::getUserId, userId)
                .eq(GameFavorite::getGameId, gameId)
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

    private Map<Long, Game> buildGameMap(List<GameFavorite> favorites) {
        Set<Long> gameIds = favorites.stream()
                .map(GameFavorite::getGameId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        if (gameIds.isEmpty()) {
            return Collections.emptyMap();
        }
        return gameService.listByIds(gameIds)
                .stream()
                .collect(Collectors.toMap(Game::getId, Function.identity(), (left, right) -> left));
    }

    private MyFavoriteVO toMyFavoriteVO(GameFavorite favorite, Game game) {
        return MyFavoriteVO.builder()
                .gameId(favorite.getGameId())
                .gameName(game == null ? null : game.getName())
                .coverUrl(game == null ? null : game.getCoverUrl())
                .description(game == null ? null : game.getDescription())
                .averageScore(game == null ? null : game.getAverageScore())
                .favoriteTime(favorite.getCreateTime())
                .build();
    }

    private FavoriteStatusVO buildStatusVO(Long gameId, boolean hasFavorited, long favoriteCount) {
        return FavoriteStatusVO.builder()
                .hasFavorited(hasFavorited)
                .gameId(gameId)
                .favoriteCount(favoriteCount)
                .build();
    }
}
