package com.gamerate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gamerate.common.exception.BusinessException;
import com.gamerate.common.result.PageResult;
import com.gamerate.common.result.ResultCode;
import com.gamerate.dto.GameCreateDTO;
import com.gamerate.dto.GameQueryDTO;
import com.gamerate.dto.GameUpdateDTO;
import com.gamerate.entity.Category;
import com.gamerate.entity.Game;
import com.gamerate.entity.Platform;
import com.gamerate.mapper.GameMapper;
import com.gamerate.service.CategoryService;
import com.gamerate.service.GameService;
import com.gamerate.service.PlatformService;
import com.gamerate.vo.GameDetailVO;
import com.gamerate.vo.GameListVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameServiceImpl extends ServiceImpl<GameMapper, Game> implements GameService {

    private static final int STATUS_ENABLED = 1;

    private static final int STATUS_DISABLED = 0;

    private static final String SORT_LATEST = "latest";

    private static final String SORT_SCORE = "score";

    private static final String SORT_HOT = "hot";

    private static final String DEFAULT_SOURCE_TYPE = "manual";

    private final CategoryService categoryService;

    private final PlatformService platformService;

    @Override
    public PageResult<GameListVO> pageVisibleGames(GameQueryDTO queryDTO) {
        LambdaQueryWrapper<Game> wrapper = buildQueryWrapper(queryDTO, false);
        return pageGames(queryDTO, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public GameDetailVO getVisibleGameDetail(Long id) {
        Game game = getById(id);
        if (game == null || !Integer.valueOf(STATUS_ENABLED).equals(game.getStatus())) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "Game not found");
        }

        baseMapper.incrementViewCount(id);
        game.setViewCount(defaultInt(game.getViewCount()) + 1);
        return toDetailVO(game);
    }

    @Override
    public PageResult<GameListVO> pageAdminGames(GameQueryDTO queryDTO) {
        LambdaQueryWrapper<Game> wrapper = buildQueryWrapper(queryDTO, true);
        return pageGames(queryDTO, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public GameDetailVO createGame(GameCreateDTO createDTO) {
        validateCategoryAndPlatform(createDTO.getCategoryId(), createDTO.getPlatformId());

        Game game = new Game();
        copyCreateFields(createDTO, game);
        game.setAverageScore(BigDecimal.ZERO);
        game.setRatingCount(0);
        game.setViewCount(0);
        game.setCommentCount(0);
        game.setFavoriteCount(0);
        game.setHotScore(BigDecimal.ZERO);
        game.setStatus(createDTO.getStatus() == null ? STATUS_ENABLED : createDTO.getStatus());
        game.setIsDeleted(0);
        save(game);

        return toDetailVO(getById(game.getId()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public GameDetailVO updateGame(Long id, GameUpdateDTO updateDTO) {
        Game game = getById(id);
        if (game == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "Game not found");
        }
        validateCategoryAndPlatform(updateDTO.getCategoryId(), updateDTO.getPlatformId());

        copyUpdateFields(updateDTO, game);
        updateById(game);
        return toDetailVO(getById(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteGame(Long id) {
        Game game = getById(id);
        if (game == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "Game not found");
        }
        game.setStatus(STATUS_DISABLED);
        updateById(game);
    }

    private PageResult<GameListVO> pageGames(GameQueryDTO queryDTO, LambdaQueryWrapper<Game> wrapper) {
        long pageNum = queryDTO.getPageNum() == null ? 1L : queryDTO.getPageNum();
        long pageSize = queryDTO.getPageSize() == null ? 10L : queryDTO.getPageSize();
        Page<Game> page = page(new Page<>(pageNum, pageSize), wrapper);

        List<Game> games = page.getRecords();
        Map<Long, String> categoryNameMap = buildCategoryNameMap(games.stream()
                .map(Game::getCategoryId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet()));
        Map<Long, String> platformNameMap = buildPlatformNameMap(games.stream()
                .map(Game::getPlatformId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet()));

        List<GameListVO> records = games.stream()
                .map(game -> toListVO(game, categoryNameMap, platformNameMap))
                .toList();
        return PageResult.of(records, page.getTotal(), page.getCurrent(), page.getSize());
    }

    private LambdaQueryWrapper<Game> buildQueryWrapper(GameQueryDTO queryDTO, boolean admin) {
        LambdaQueryWrapper<Game> wrapper = new LambdaQueryWrapper<>();
        if (!admin) {
            wrapper.eq(Game::getStatus, STATUS_ENABLED);
        }
        if (StringUtils.hasText(queryDTO.getKeyword())) {
            String keyword = queryDTO.getKeyword().trim();
            wrapper.and(query -> query.like(Game::getName, keyword)
                    .or()
                    .like(Game::getOriginalName, keyword));
        }
        if (queryDTO.getCategoryId() != null) {
            wrapper.eq(Game::getCategoryId, queryDTO.getCategoryId());
        }
        if (queryDTO.getPlatformId() != null) {
            wrapper.eq(Game::getPlatformId, queryDTO.getPlatformId());
        }

        applySort(wrapper, queryDTO.getSort());
        return wrapper;
    }

    private void applySort(LambdaQueryWrapper<Game> wrapper, String sort) {
        String sortValue = StringUtils.hasText(sort) ? sort.trim() : "default";
        switch (sortValue) {
            case SORT_LATEST -> wrapper.orderByDesc(Game::getReleaseDate).orderByDesc(Game::getCreateTime);
            case SORT_SCORE -> wrapper.orderByDesc(Game::getAverageScore).orderByDesc(Game::getRatingCount);
            case SORT_HOT -> wrapper.orderByDesc(Game::getHotScore).orderByDesc(Game::getViewCount);
            default -> wrapper.orderByDesc(Game::getCreateTime).orderByDesc(Game::getId);
        }
    }

    private void validateCategoryAndPlatform(Long categoryId, Long platformId) {
        if (categoryService.getById(categoryId) == null) {
            throw new BusinessException("Game category does not exist");
        }
        if (platformService.getById(platformId) == null) {
            throw new BusinessException("Game platform does not exist");
        }
    }

    private void copyCreateFields(GameCreateDTO dto, Game game) {
        game.setName(dto.getName().trim());
        game.setOriginalName(trimToNull(dto.getOriginalName()));
        game.setDescription(dto.getDescription());
        game.setDeveloper(trimToNull(dto.getDeveloper()));
        game.setPublisher(trimToNull(dto.getPublisher()));
        game.setReleaseDate(dto.getReleaseDate());
        game.setCategoryId(dto.getCategoryId());
        game.setPlatformId(dto.getPlatformId());
        game.setSourceType(StringUtils.hasText(dto.getSourceType()) ? dto.getSourceType().trim() : DEFAULT_SOURCE_TYPE);
        game.setSourceId(trimToNull(dto.getSourceId()));
        game.setSourceUrl(trimToNull(dto.getSourceUrl()));
        game.setSteamAppId(dto.getSteamAppId());
        game.setCoverUrl(trimToNull(dto.getCoverUrl()));
        game.setBackgroundUrl(trimToNull(dto.getBackgroundUrl()));
        game.setRawgRating(dto.getRawgRating());
        game.setMetacriticScore(dto.getMetacriticScore());
    }

    private void copyUpdateFields(GameUpdateDTO dto, Game game) {
        game.setName(dto.getName().trim());
        game.setOriginalName(trimToNull(dto.getOriginalName()));
        game.setDescription(dto.getDescription());
        game.setDeveloper(trimToNull(dto.getDeveloper()));
        game.setPublisher(trimToNull(dto.getPublisher()));
        game.setReleaseDate(dto.getReleaseDate());
        game.setCategoryId(dto.getCategoryId());
        game.setPlatformId(dto.getPlatformId());
        game.setSourceType(StringUtils.hasText(dto.getSourceType()) ? dto.getSourceType().trim() : DEFAULT_SOURCE_TYPE);
        game.setSourceId(trimToNull(dto.getSourceId()));
        game.setSourceUrl(trimToNull(dto.getSourceUrl()));
        game.setSteamAppId(dto.getSteamAppId());
        game.setCoverUrl(trimToNull(dto.getCoverUrl()));
        game.setBackgroundUrl(trimToNull(dto.getBackgroundUrl()));
        game.setRawgRating(dto.getRawgRating());
        game.setMetacriticScore(dto.getMetacriticScore());
        if (dto.getStatus() != null) {
            game.setStatus(dto.getStatus());
        }
    }

    private GameListVO toListVO(Game game, Map<Long, String> categoryNameMap, Map<Long, String> platformNameMap) {
        return GameListVO.builder()
                .id(game.getId())
                .name(game.getName())
                .originalName(game.getOriginalName())
                .coverUrl(game.getCoverUrl())
                .releaseDate(game.getReleaseDate())
                .categoryId(game.getCategoryId())
                .categoryName(categoryNameMap.get(game.getCategoryId()))
                .platformId(game.getPlatformId())
                .platformName(platformNameMap.get(game.getPlatformId()))
                .averageScore(game.getAverageScore())
                .ratingCount(game.getRatingCount())
                .viewCount(game.getViewCount())
                .favoriteCount(game.getFavoriteCount())
                .commentCount(game.getCommentCount())
                .hotScore(game.getHotScore())
                .status(game.getStatus())
                .build();
    }

    private GameDetailVO toDetailVO(Game game) {
        Category category = game.getCategoryId() == null ? null : categoryService.getById(game.getCategoryId());
        Platform platform = game.getPlatformId() == null ? null : platformService.getById(game.getPlatformId());
        return GameDetailVO.builder()
                .id(game.getId())
                .name(game.getName())
                .originalName(game.getOriginalName())
                .description(game.getDescription())
                .developer(game.getDeveloper())
                .publisher(game.getPublisher())
                .releaseDate(game.getReleaseDate())
                .categoryId(game.getCategoryId())
                .categoryName(category == null ? null : category.getName())
                .platformId(game.getPlatformId())
                .platformName(platform == null ? null : platform.getName())
                .sourceType(game.getSourceType())
                .sourceId(game.getSourceId())
                .sourceUrl(game.getSourceUrl())
                .steamAppId(game.getSteamAppId())
                .coverUrl(game.getCoverUrl())
                .backgroundUrl(game.getBackgroundUrl())
                .rawgRating(game.getRawgRating())
                .metacriticScore(game.getMetacriticScore())
                .lastSyncTime(game.getLastSyncTime())
                .averageScore(game.getAverageScore())
                .ratingCount(game.getRatingCount())
                .viewCount(game.getViewCount())
                .commentCount(game.getCommentCount())
                .favoriteCount(game.getFavoriteCount())
                .hotScore(game.getHotScore())
                .status(game.getStatus())
                .createTime(game.getCreateTime())
                .updateTime(game.getUpdateTime())
                .build();
    }

    private Map<Long, String> buildCategoryNameMap(Set<Long> categoryIds) {
        if (categoryIds.isEmpty()) {
            return Collections.emptyMap();
        }
        return categoryService.listByIds(categoryIds)
                .stream()
                .collect(Collectors.toMap(Category::getId, Category::getName, (left, right) -> left));
    }

    private Map<Long, String> buildPlatformNameMap(Set<Long> platformIds) {
        if (platformIds.isEmpty()) {
            return Collections.emptyMap();
        }
        return platformService.listByIds(platformIds)
                .stream()
                .collect(Collectors.toMap(Platform::getId, Platform::getName, (left, right) -> left));
    }

    private String trimToNull(String value) {
        return StringUtils.hasText(value) ? value.trim() : null;
    }

    private int defaultInt(Integer value) {
        return value == null ? 0 : value;
    }
}
