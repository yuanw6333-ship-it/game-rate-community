package com.gamerate.service.impl;

import com.gamerate.client.RawgClient;
import com.gamerate.common.exception.BusinessException;
import com.gamerate.dto.RawgImportDTO;
import com.gamerate.dto.RawgSearchDTO;
import com.gamerate.entity.Game;
import com.gamerate.service.CategoryService;
import com.gamerate.service.GameService;
import com.gamerate.service.PlatformService;
import com.gamerate.service.RawgService;
import com.gamerate.vo.RawgGameDetailVO;
import com.gamerate.vo.RawgGameSearchVO;
import com.gamerate.vo.RawgImportResultVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RawgServiceImpl implements RawgService {

    private static final String SOURCE_TYPE_RAWG = "RAWG";

    private static final int STATUS_ENABLED = 1;

    private final RawgClient rawgClient;

    private final GameService gameService;

    private final CategoryService categoryService;

    private final PlatformService platformService;

    @Override
    public List<RawgGameSearchVO> searchGames(RawgSearchDTO searchDTO) {
        return rawgClient.searchGames(searchDTO.getKeyword(), searchDTO.getPageNum(), searchDTO.getPageSize());
    }

    @Override
    public RawgGameDetailVO getGameDetail(Long rawgId) {
        return rawgClient.getGameDetail(rawgId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RawgImportResultVO importGame(RawgImportDTO importDTO) {
        validateCategoryAndPlatform(importDTO.getCategoryId(), importDTO.getPlatformId());

        Game existing = gameService.lambdaQuery()
                .eq(Game::getSourceType, SOURCE_TYPE_RAWG)
                .eq(Game::getSourceId, importDTO.getRawgId().toString())
                .one();
        if (existing != null) {
            return RawgImportResultVO.builder()
                    .gameId(existing.getId())
                    .imported(false)
                    .existed(true)
                    .message("Game already exists, import skipped")
                    .build();
        }

        RawgGameDetailVO detail = rawgClient.getGameDetail(importDTO.getRawgId());
        if (detail == null || detail.getRawgId() == null || !StringUtils.hasText(detail.getName())) {
            throw new BusinessException("RAWG game detail is empty");
        }

        Game game = buildGame(importDTO, detail);
        if (!gameService.save(game)) {
            throw new BusinessException("Failed to import RAWG game");
        }
        return RawgImportResultVO.builder()
                .gameId(game.getId())
                .imported(true)
                .existed(false)
                .message("Game imported successfully")
                .build();
    }

    private void validateCategoryAndPlatform(Long categoryId, Long platformId) {
        if (categoryService.getById(categoryId) == null) {
            throw new BusinessException("Game category does not exist");
        }
        if (platformService.getById(platformId) == null) {
            throw new BusinessException("Game platform does not exist");
        }
    }

    private Game buildGame(RawgImportDTO importDTO, RawgGameDetailVO detail) {
        Game game = new Game();
        game.setName(truncate(detail.getName(), 100));
        game.setDescription(detail.getDescription());
        game.setDeveloper(joinAndTruncate(detail.getDevelopers(), 100));
        game.setPublisher(joinAndTruncate(detail.getPublishers(), 100));
        game.setReleaseDate(parseDate(detail.getReleased()));
        game.setCategoryId(importDTO.getCategoryId());
        game.setPlatformId(importDTO.getPlatformId());
        game.setSourceType(SOURCE_TYPE_RAWG);
        game.setSourceId(detail.getRawgId().toString());
        game.setSourceUrl(buildSourceUrl(detail));
        game.setCoverUrl(truncate(detail.getBackgroundImage(), 500));
        game.setBackgroundUrl(truncate(detail.getBackgroundImage(), 500));
        game.setRawgRating(detail.getRating());
        game.setMetacriticScore(detail.getMetacritic());
        game.setLastSyncTime(LocalDateTime.now());
        game.setAverageScore(BigDecimal.ZERO);
        game.setRatingCount(0);
        game.setViewCount(0);
        game.setCommentCount(0);
        game.setFavoriteCount(0);
        game.setHotScore(BigDecimal.ZERO);
        game.setStatus(importDTO.getStatus() == null ? STATUS_ENABLED : importDTO.getStatus());
        game.setIsDeleted(0);
        return game;
    }

    private String buildSourceUrl(RawgGameDetailVO detail) {
        if (StringUtils.hasText(detail.getSlug())) {
            return truncate("https://rawg.io/games/" + detail.getSlug().trim(), 500);
        }
        return "https://rawg.io/games/" + detail.getRawgId();
    }

    private LocalDate parseDate(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        try {
            return LocalDate.parse(value.trim());
        } catch (DateTimeParseException exception) {
            return null;
        }
    }

    private String joinAndTruncate(List<String> values, int maxLength) {
        if (values == null || values.isEmpty()) {
            return null;
        }
        return truncate(String.join(", ", values), maxLength);
    }

    private String truncate(String value, int maxLength) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.length() <= maxLength ? trimmed : trimmed.substring(0, maxLength);
    }
}
