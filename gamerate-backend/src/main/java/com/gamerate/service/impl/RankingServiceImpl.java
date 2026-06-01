package com.gamerate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gamerate.common.result.PageResult;
import com.gamerate.entity.Game;
import com.gamerate.service.GameService;
import com.gamerate.service.RankingService;
import com.gamerate.vo.GameRankingVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class RankingServiceImpl implements RankingService {

    private static final int STATUS_ENABLED = 1;

    private final GameService gameService;

    @Override
    public PageResult<GameRankingVO> pageTopScoreGames(Integer pageNum, Integer pageSize) {
        return pageGames(pageNum, pageSize, wrapper -> wrapper
                .orderByDesc(Game::getAverageScore)
                .orderByDesc(Game::getRatingCount)
                .orderByDesc(Game::getId));
    }

    @Override
    public PageResult<GameRankingVO> pagePopularGames(Integer pageNum, Integer pageSize) {
        return pageGames(pageNum, pageSize, wrapper -> wrapper
                .orderByDesc(Game::getViewCount)
                .orderByDesc(Game::getFavoriteCount)
                .orderByDesc(Game::getCommentCount)
                .orderByDesc(Game::getId));
    }

    @Override
    public PageResult<GameRankingVO> pageFavoriteGames(Integer pageNum, Integer pageSize) {
        return pageGames(pageNum, pageSize, wrapper -> wrapper
                .orderByDesc(Game::getFavoriteCount)
                .orderByDesc(Game::getViewCount)
                .orderByDesc(Game::getId));
    }

    @Override
    public PageResult<GameRankingVO> pageCommentGames(Integer pageNum, Integer pageSize) {
        return pageGames(pageNum, pageSize, wrapper -> wrapper
                .orderByDesc(Game::getCommentCount)
                .orderByDesc(Game::getViewCount)
                .orderByDesc(Game::getId));
    }

    @Override
    public PageResult<GameRankingVO> pageNewGames(Integer pageNum, Integer pageSize) {
        return pageGames(pageNum, pageSize, wrapper -> wrapper
                .orderByDesc(Game::getReleaseDate)
                .orderByDesc(Game::getCreateTime)
                .orderByDesc(Game::getId));
    }

    private PageResult<GameRankingVO> pageGames(
            Integer pageNum,
            Integer pageSize,
            Consumer<LambdaQueryWrapper<Game>> sortCustomizer) {
        long current = pageNum == null ? 1L : pageNum;
        long size = pageSize == null ? 10L : pageSize;
        LambdaQueryWrapper<Game> wrapper = new LambdaQueryWrapper<Game>()
                .eq(Game::getStatus, STATUS_ENABLED);
        sortCustomizer.accept(wrapper);

        Page<Game> page = gameService.page(new Page<>(current, size), wrapper);
        List<GameRankingVO> records = page.getRecords()
                .stream()
                .map(this::toRankingVO)
                .toList();
        return PageResult.of(records, page.getTotal(), page.getCurrent(), page.getSize());
    }

    private GameRankingVO toRankingVO(Game game) {
        return GameRankingVO.builder()
                .gameId(game.getId())
                .name(game.getName())
                .coverUrl(game.getCoverUrl())
                .averageScore(game.getAverageScore())
                .ratingCount(game.getRatingCount())
                .favoriteCount(game.getFavoriteCount())
                .commentCount(game.getCommentCount())
                .viewCount(game.getViewCount())
                .releaseDate(game.getReleaseDate())
                .build();
    }
}
