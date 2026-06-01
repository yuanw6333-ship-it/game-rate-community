package com.gamerate.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gamerate.entity.GameFavorite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface GameFavoriteMapper extends BaseMapper<GameFavorite> {

    @Select("""
            SELECT COUNT(*)
            FROM game_favorite
            WHERE game_id = #{gameId}
              AND status = 1
              AND is_deleted = 0
            """)
    long countActiveFavoritesByGameId(@Param("gameId") Long gameId);

    @Select("""
            SELECT gf.*
            FROM game_favorite gf
            INNER JOIN game
              ON game.id = gf.game_id
             AND game.status = 1
             AND game.is_deleted = 0
            WHERE gf.user_id = #{userId}
              AND gf.status = 1
              AND gf.is_deleted = 0
            ORDER BY gf.create_time DESC, gf.id DESC
            """)
    IPage<GameFavorite> selectMyFavoritePage(Page<GameFavorite> page, @Param("userId") Long userId);
}
