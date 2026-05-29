package com.gamerate.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gamerate.entity.Game;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface GameMapper extends BaseMapper<Game> {

    @Update("""
            UPDATE game
            SET view_count = view_count + 1
            WHERE id = #{id}
              AND status = 1
              AND is_deleted = 0
            """)
    int incrementViewCount(@Param("id") Long id);
}
