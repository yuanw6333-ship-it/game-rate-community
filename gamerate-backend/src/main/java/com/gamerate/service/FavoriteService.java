package com.gamerate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gamerate.common.result.PageResult;
import com.gamerate.entity.GameFavorite;
import com.gamerate.vo.FavoriteStatusVO;
import com.gamerate.vo.MyFavoriteVO;

public interface FavoriteService extends IService<GameFavorite> {

    FavoriteStatusVO favoriteGame(Long gameId);

    FavoriteStatusVO unfavoriteGame(Long gameId);

    FavoriteStatusVO getFavoriteStatus(Long gameId);

    PageResult<MyFavoriteVO> pageMyFavorites(Integer pageNum, Integer pageSize);
}
