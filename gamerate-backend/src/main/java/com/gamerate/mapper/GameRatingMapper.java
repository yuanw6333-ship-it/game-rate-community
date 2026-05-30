package com.gamerate.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gamerate.entity.GameRating;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

@Mapper
public interface GameRatingMapper extends BaseMapper<GameRating> {

    @Select("""
            SELECT COUNT(*)
            FROM game_rating
            WHERE game_id = #{gameId}
              AND status = 1
              AND is_deleted = 0
            """)
    long countActiveRatingsByGameId(@Param("gameId") Long gameId);

    @Select("""
            SELECT AVG(score)
            FROM game_rating
            WHERE game_id = #{gameId}
              AND status = 1
              AND is_deleted = 0
            """)
    BigDecimal selectAverageScoreByGameId(@Param("gameId") Long gameId);
}
