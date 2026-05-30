package com.gamerate.service;

import com.gamerate.dto.RawgImportDTO;
import com.gamerate.dto.RawgSearchDTO;
import com.gamerate.vo.RawgGameDetailVO;
import com.gamerate.vo.RawgGameSearchVO;
import com.gamerate.vo.RawgImportResultVO;

import java.util.List;

public interface RawgService {

    List<RawgGameSearchVO> searchGames(RawgSearchDTO searchDTO);

    RawgGameDetailVO getGameDetail(Long rawgId);

    RawgImportResultVO importGame(RawgImportDTO importDTO);
}
