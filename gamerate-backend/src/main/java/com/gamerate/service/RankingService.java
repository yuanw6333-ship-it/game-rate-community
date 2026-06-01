package com.gamerate.service;

import com.gamerate.common.result.PageResult;
import com.gamerate.vo.GameRankingVO;

public interface RankingService {

    PageResult<GameRankingVO> pageTopScoreGames(Integer pageNum, Integer pageSize);

    PageResult<GameRankingVO> pagePopularGames(Integer pageNum, Integer pageSize);

    PageResult<GameRankingVO> pageFavoriteGames(Integer pageNum, Integer pageSize);

    PageResult<GameRankingVO> pageCommentGames(Integer pageNum, Integer pageSize);

    PageResult<GameRankingVO> pageNewGames(Integer pageNum, Integer pageSize);
}
