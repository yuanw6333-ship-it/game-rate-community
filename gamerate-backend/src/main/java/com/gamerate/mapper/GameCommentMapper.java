package com.gamerate.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gamerate.entity.GameComment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface GameCommentMapper extends BaseMapper<GameComment> {

    @Select("""
            SELECT COUNT(*)
            FROM game_comment
            WHERE game_id = #{gameId}
              AND status = 1
              AND is_deleted = 0
            """)
    long countActiveCommentsByGameId(@Param("gameId") Long gameId);
}
