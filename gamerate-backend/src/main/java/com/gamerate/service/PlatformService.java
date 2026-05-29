package com.gamerate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gamerate.entity.Platform;
import com.gamerate.vo.PlatformVO;

import java.util.List;

public interface PlatformService extends IService<Platform> {

    List<PlatformVO> listEnabledPlatforms();
}
