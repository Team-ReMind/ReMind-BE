package com.remind.infra.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private final String SCHEME_NAME = "Authorization";
    private final String BEARER_FORMAT = "JWT";
    private final String SCHEME = "Bearer";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement()
                        .addList(SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(
                                SCHEME_NAME, new SecurityScheme()
                                        .name(SCHEME_NAME)
                                        .type(Type.HTTP)
                                        .bearerFormat(BEARER_FORMAT)
                                        .in(In.HEADER)
                                        .scheme(SCHEME)))
                .info(new Info()
                        .title("ReMind 프로젝트 API 문서")
                        .description("큐시즘 밋업 ReMind 프로젝트 API 문서")
                        .version("1.0.0"));
    }
}
