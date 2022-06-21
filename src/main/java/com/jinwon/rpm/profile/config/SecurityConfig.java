package com.jinwon.rpm.profile.config;

import com.jinwon.rpm.profile.component.TokenRedisComponent;
import com.jinwon.rpm.profile.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
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

/**
 * 시큐리티 설정
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final TokenRedisComponent tokenRedisComponent;

    @Override
    protected void configure(HttpSecurity security) throws Exception {
        security
                .csrf(AbstractHttpConfigurer::disable)
                .headers(HeadersConfigurer::frameOptions)
                .headers(HeadersConfigurer::disable)
                .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeRequests(SecurityConfig::customize)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .addFilterBefore(
                        new JwtAuthenticationFilter(tokenRedisComponent),
                        UsernamePasswordAuthenticationFilter.class);
    }

    private static void customize(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.
                                          ExpressionInterceptUrlRegistry registry) {
        registry.antMatchers("/api-docs/**", "/health", "/swagger-ui.html",
                "/swagger-ui/**", "/v3/api-docs/**", "/actuator/**")
                .permitAll()
                .antMatchers(HttpMethod.POST, "/")
                .permitAll()
                .anyRequest()
                .authenticated();
    }

}