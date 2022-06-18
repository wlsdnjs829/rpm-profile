package com.jinwon.rpm.profile.config;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.providers.ObjectMapperProvider;
import org.springdoc.webflux.core.converters.WebFluxSupportConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class OpenApiConfig {

    private final ObjectMapperProvider objectMapperProvider;

    private static final String API_NAME = "RPM_PROFILE";
    private static final String API_VERSION = "0.0.1";
    private static final String API_DESCRIPTION = "Rpm Profile API 명세서";

    private static final String JWT = "JWT";
    private static final String X_AUTH_TOKEN = "X-AUTH-TOKEN";
    private static final String BEARER = "Bearer";

    @Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                .group(API_NAME)
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    public OpenAPI springShopOpenAPI() {
        ModelConverters.getInstance().addConverter(new WebFluxSupportConverter(objectMapperProvider));

        return new OpenAPI()
                .components(new Components().addSecuritySchemes(X_AUTH_TOKEN, getSecurityScheme()))
                .security(List.of(getSecurityRequirement()))
                .info(new Info().title(API_NAME)
                        .description(API_DESCRIPTION)
                        .version(API_VERSION));
    }

    private SecurityScheme getSecurityScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.APIKEY)
                .scheme(BEARER)
                .bearerFormat(JWT)
                .in(SecurityScheme.In.HEADER)
                .name(X_AUTH_TOKEN);
    }

    private SecurityRequirement getSecurityRequirement() {
        return new SecurityRequirement().addList(X_AUTH_TOKEN);
    }

}
