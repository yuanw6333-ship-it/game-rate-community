package com.gamerate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gamerate.common.result.PageResult;
import com.gamerate.dto.RatingSubmitDTO;
import com.gamerate.entity.GameRating;
import com.gamerate.vo.GameRatingVO;
import com.gamerate.vo.MyRatingVO;

public interface RatingService extends IService<GameRating> {

    GameRatingVO submitRating(RatingSubmitDTO submitDTO);

    MyRatingVO getMyRating(Long gameId);

    PageResult<MyRatingVO> pageMyRatings(Integer pageNum, Integer pageSize);
}
