package com.gamerate.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.gamerate.common.exception.BusinessException;
import com.gamerate.config.RawgProperties;
import com.gamerate.vo.RawgGameDetailVO;
import com.gamerate.vo.RawgGameSearchVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.HtmlUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class RawgClient {

    private static final String API_KEY_PLACEHOLDER = "RAWG_API_KEY";

    private static final String API_KEY_REDACTED = "REDACTED";

    private final RestTemplate restTemplate;

    private final RawgProperties rawgProperties;

    public List<RawgGameSearchVO> searchGames(String keyword, Integer pageNum, Integer pageSize) {
        if (!StringUtils.hasText(keyword)) {
            throw new BusinessException("RAWG search keyword cannot be blank");
        }
        URI uri = UriComponentsBuilder.fromUriString(normalizeBaseUrl())
                .pathSegment("games")
                .queryParam("key", getApiKey())
                .queryParam("search", keyword.trim())
                .queryParam("page", pageNum == null ? 1 : pageNum)
                .queryParam("page_size", pageSize == null ? 10 : pageSize)
                .build()
                .encode()
                .toUri();

        JsonNode response = get(uri);
        JsonNode results = response.path("results");
        if (!results.isArray()) {
            return Collections.emptyList();
        }

        List<RawgGameSearchVO> games = new ArrayList<>();
        for (JsonNode item : results) {
            games.add(toSearchVO(item));
        }
        return games;
    }

    public RawgGameDetailVO getGameDetail(Long rawgId) {
        if (rawgId == null) {
            throw new BusinessException("RAWG game id cannot be null");
        }
        URI uri = UriComponentsBuilder.fromUriString(normalizeBaseUrl())
                .pathSegment("games", rawgId.toString())
                .queryParam("key", getApiKey())
                .build()
                .encode()
                .toUri();

        JsonNode response = get(uri);
        if (response == null || response.isNull() || response.isMissingNode()) {
            throw new BusinessException("RAWG game detail is empty");
        }
        return toDetailVO(response);
    }

    private JsonNode get(URI uri) {
        String sanitizedUrl = sanitizeUrl(uri);
        log.info("RAWG request: method=GET, url={}, keyPresent={}", sanitizedUrl, hasApiKey(uri));
        try {
            JsonNode response = restTemplate.getForObject(uri, JsonNode.class);
            if (response == null || response.isNull() || response.isMissingNode()) {
                throw new BusinessException("RAWG response is empty");
            }
            return response;
        } catch (BusinessException exception) {
            throw exception;
        } catch (ResourceAccessException exception) {
            log.warn("RAWG request timeout or network error: url={}", sanitizedUrl);
            throw new BusinessException("RAWG request timeout or network error. Please check network access and timeout settings");
        } catch (HttpClientErrorException exception) {
            log.warn("RAWG client error: url={}, status={}", sanitizedUrl, exception.getStatusCode().value());
            if (exception.getStatusCode().value() == 404) {
                throw new BusinessException("RAWG game not found");
            }
            if (exception.getStatusCode().value() == 401 || exception.getStatusCode().value() == 403) {
                throw new BusinessException("RAWG API key is invalid or access is denied");
            }
            throw new BusinessException("RAWG client request failed, status: " + exception.getStatusCode().value());
        } catch (HttpServerErrorException exception) {
            log.warn("RAWG server error: url={}, status={}", sanitizedUrl, exception.getStatusCode().value());
            throw new BusinessException("RAWG server error, status: " + exception.getStatusCode().value());
        } catch (RestClientException exception) {
            log.warn("RAWG request failed: url={}, exception={}", sanitizedUrl, exception.getClass().getSimpleName());
            throw new BusinessException("RAWG request failed: " + exception.getClass().getSimpleName());
        }
    }

    private String sanitizeUrl(URI uri) {
        return UriComponentsBuilder.fromUri(uri)
                .replaceQueryParam("key", API_KEY_REDACTED)
                .build(true)
                .toUriString();
    }

    private boolean hasApiKey(URI uri) {
        String key = UriComponentsBuilder.fromUri(uri)
                .build(true)
                .getQueryParams()
                .getFirst("key");
        return StringUtils.hasText(key);
    }

    private RawgGameSearchVO toSearchVO(JsonNode item) {
        return RawgGameSearchVO.builder()
                .rawgId(longValue(item, "id"))
                .name(textValue(item, "name"))
                .released(textValue(item, "released"))
                .backgroundImage(textValue(item, "background_image"))
                .rating(decimalValue(item, "rating"))
                .metacritic(intValue(item, "metacritic"))
                .platforms(nameList(item.path("platforms"), "platform"))
                .genres(nameList(item.path("genres"), null))
                .build();
    }

    private RawgGameDetailVO toDetailVO(JsonNode item) {
        String description = textValue(item, "description_raw");
        if (!StringUtils.hasText(description)) {
            description = textValue(item, "description");
        }

        return RawgGameDetailVO.builder()
                .rawgId(longValue(item, "id"))
                .slug(textValue(item, "slug"))
                .name(textValue(item, "name"))
                .description(cleanText(description))
                .released(textValue(item, "released"))
                .backgroundImage(textValue(item, "background_image"))
                .website(textValue(item, "website"))
                .rating(decimalValue(item, "rating"))
                .metacritic(intValue(item, "metacritic"))
                .developers(nameList(item.path("developers"), null))
                .publishers(nameList(item.path("publishers"), null))
                .platforms(nameList(item.path("platforms"), "platform"))
                .genres(nameList(item.path("genres"), null))
                .build();
    }

    private List<String> nameList(JsonNode array, String nestedField) {
        if (!array.isArray()) {
            return Collections.emptyList();
        }

        List<String> names = new ArrayList<>();
        for (JsonNode item : array) {
            JsonNode source = StringUtils.hasText(nestedField) ? item.path(nestedField) : item;
            String name = textValue(source, "name");
            if (StringUtils.hasText(name) && !names.contains(name)) {
                names.add(name);
            }
        }
        return names;
    }

    private Long longValue(JsonNode node, String fieldName) {
        JsonNode value = node.path(fieldName);
        return value.isNumber() ? value.asLong() : null;
    }

    private Integer intValue(JsonNode node, String fieldName) {
        JsonNode value = node.path(fieldName);
        return value.isNumber() ? value.asInt() : null;
    }

    private BigDecimal decimalValue(JsonNode node, String fieldName) {
        JsonNode value = node.path(fieldName);
        if (!value.isNumber()) {
            return null;
        }
        return BigDecimal.valueOf(value.asDouble()).setScale(2, RoundingMode.HALF_UP);
    }

    private String textValue(JsonNode node, String fieldName) {
        JsonNode value = node.path(fieldName);
        if (value.isMissingNode() || value.isNull()) {
            return null;
        }
        String text = value.asText();
        return StringUtils.hasText(text) ? text.trim() : null;
    }

    private String cleanText(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        String unescaped = HtmlUtils.htmlUnescape(value);
        String withoutTags = unescaped.replaceAll("<[^>]+>", " ");
        String normalized = withoutTags.replaceAll("\\s+", " ").trim();
        return StringUtils.hasText(normalized) ? normalized : null;
    }

    private String normalizeBaseUrl() {
        String baseUrl = rawgProperties.getBaseUrl();
        if (!StringUtils.hasText(baseUrl)) {
            throw new BusinessException("RAWG base url is not configured");
        }
        String normalized = baseUrl.trim();
        while (normalized.endsWith("/")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }
        return normalized;
    }

    private String getApiKey() {
        String apiKey = rawgProperties.getApiKey();
        if (!StringUtils.hasText(apiKey) || apiKey.contains(API_KEY_PLACEHOLDER)) {
            throw new BusinessException("RAWG API key is not configured");
        }
        return apiKey.trim();
    }
}
