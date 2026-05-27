package com.gamerate.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Knife4jConfig {

    @Bean
    public OpenAPI gameRateOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("GameRate API")
                        .description("GameRate 游戏评分社区后端接口文档")
                        .version("v1.0.0"));
    }

    @Bean
    public GroupedOpenApi gameRateApiGroup() {
        return GroupedOpenApi.builder()
                .group("gamerate-v1")
                .pathsToMatch("/api/**")
                .build();
    }
}
