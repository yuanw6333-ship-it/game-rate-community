package com.gamerate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gamerate.common.result.PageResult;
import com.gamerate.dto.GameCreateDTO;
import com.gamerate.dto.GameQueryDTO;
import com.gamerate.dto.GameUpdateDTO;
import com.gamerate.entity.Game;
import com.gamerate.vo.GameDetailVO;
import com.gamerate.vo.GameListVO;

public interface GameService extends IService<Game> {

    PageResult<GameListVO> pageVisibleGames(GameQueryDTO queryDTO);

    GameDetailVO getVisibleGameDetail(Long id);

    PageResult<GameListVO> pageAdminGames(GameQueryDTO queryDTO);

    GameDetailVO createGame(GameCreateDTO createDTO);

    GameDetailVO updateGame(Long id, GameUpdateDTO updateDTO);

    void deleteGame(Long id);
}
