package com.jinwon.rpm.profile.infra.config;

import com.jinwon.rpm.profile.infra.component.TokenRedisComponent;
import com.jinwon.rpm.profile.infra.config.jwt.JwtAuthenticationFilter;
import com.jinwon.rpm.profile.infra.config.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * 시큐리티 설정
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;
    private final TokenRedisComponent tokenRedisComponent;

    private static final String WILD_CARD = "*";
    private static final String WILD_PATTERN = "/**";

    @Override
    protected void configure(HttpSecurity security) throws Exception {
        security
                .cors()
                .and()
                .csrf(AbstractHttpConfigurer::disable)
                .headers(HeadersConfigurer::frameOptions)
                .headers(HeadersConfigurer::disable)
                .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeRequests(SecurityConfig::customize)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .addFilterBefore(
                        new JwtAuthenticationFilter(jwtTokenProvider, tokenRedisComponent),
                        UsernamePasswordAuthenticationFilter.class);
    }

    private static void customize(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.
                                          ExpressionInterceptUrlRegistry registry) {
        registry.antMatchers("/api-docs/**", "/health", "/swagger-ui.html",
                "/swagger-ui/**", "/v3/api-docs/**", "/actuator/**", "/h2-console/**")
                .permitAll()
                .antMatchers("/v1/auth/login", "/v1/auth/refresh-token")
                .permitAll()
                .antMatchers(HttpMethod.POST, "/v1")
                .permitAll()
                .anyRequest()
                .authenticated();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOriginPattern(WILD_CARD);
        configuration.addAllowedHeader(WILD_CARD);
        configuration.addAllowedMethod(WILD_CARD);
        configuration.setAllowCredentials(true);

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(WILD_PATTERN, configuration);
        return source;
    }

}