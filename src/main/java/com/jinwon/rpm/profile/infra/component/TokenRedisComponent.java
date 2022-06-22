package com.jinwon.rpm.profile.infra.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jinwon.rpm.profile.domain.profile.Profile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 토큰 레디스 기능 컴포넌트
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TokenRedisComponent {

    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, String> redisTemplate;

    private static final String JSON_PARSING_ERROR = "Json Parsing Error";

    /**
     * 토큰 프로필 조회
     *
     * @param token accessToken & refreshToken
     */
    public Optional<Profile> getTokenProfile(String token) {
        final ValueOperations<String, String> values = redisTemplate.opsForValue();
        final String content = values.get(token);

        if (StringUtils.isEmpty(content)) {
            return Optional.empty();
        }

        return getProfile(content);
    }

    /* json 데이터 프로필 정보 변환 */
    private Optional<Profile> getProfile(String content) {
        try {
            return Optional.of(
                    objectMapper.readValue(content, Profile.class));
        } catch (JsonProcessingException e) {
            log.error(ExceptionUtils.getStackTrace(e));
            log.error(JSON_PARSING_ERROR);
            return Optional.empty();
        }
    }

}
