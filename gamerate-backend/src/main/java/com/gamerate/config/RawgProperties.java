package com.gamerate.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "rawg")
public class RawgProperties {

    private String apiKey;

    private String baseUrl;
}
