package com.gamerate.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gamerate.entity.Platform;
import com.gamerate.mapper.PlatformMapper;
import com.gamerate.service.PlatformService;
import com.gamerate.vo.PlatformVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlatformServiceImpl extends ServiceImpl<PlatformMapper, Platform> implements PlatformService {

    private static final int STATUS_ENABLED = 1;

    @Override
    public List<PlatformVO> listEnabledPlatforms() {
        return lambdaQuery()
                .eq(Platform::getStatus, STATUS_ENABLED)
                .orderByAsc(Platform::getSortOrder)
                .orderByAsc(Platform::getId)
                .list()
                .stream()
                .map(this::toVO)
                .toList();
    }

    private PlatformVO toVO(Platform platform) {
        return PlatformVO.builder()
                .id(platform.getId())
                .name(platform.getName())
                .code(platform.getCode())
                .description(platform.getDescription())
                .sortOrder(platform.getSortOrder())
                .build();
    }
}
