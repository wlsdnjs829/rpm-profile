package com.jinwon.rpm.profile.infra.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jinwon.rpm.profile.constants.enums.ErrorMessage;
import com.jinwon.rpm.profile.domain.mail.inner_dto.CertInfoDto;
import com.jinwon.rpm.profile.domain.member.Member;
import com.jinwon.rpm.profile.infra.exception.CustomException;
import com.jinwon.rpm.profile.infra.utils.EncryptionUtil;
import io.jsonwebtoken.lang.Assert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;

/**
 * 레디스 기능 컴포넌트
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RedisComponent {

    @Value("${spring.security.oauth2.jwt.expired}")
    private int tokenExpired;

    @Value("${spring.mail.cert.expired}")
    private int certMailExpired;

    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, String> redisTemplate;

    private static final String CHANGE_MAIL_PREFIX = "CHANGE_MAIL_";

    private static final String JSON_PARSING_ERROR = "Json Parsing Error";

    /**
     * 레디스 토큰 정보 저장
     *
     * @param token  accessToken
     * @param member 사용자 정보
     */
    public void addAccessToken(String token, Member member) {
        if (Objects.isNull(token) || Objects.isNull(member)) {
            return;
        }

        final String json = parserJacksonString(member);
        final ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(token, json, Duration.ofHours(tokenExpired));
    }

    /**
     * 레디스 재설정 토큰 저장
     *
     * @param refreshToken 재설정 토큰
     * @param member       사용자 정보
     */
    public void addRefreshToken(String refreshToken, Member member) {
        if (Objects.isNull(refreshToken) || Objects.isNull(member)) {
            return;
        }

        final String json = parserJacksonString(member);
        final ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(refreshToken, json, Duration.ofDays(tokenExpired));
    }

    /**
     * 토큰 사용자 조회
     *
     * @param token accessToken & refreshToken
     */
    public Optional<Member> getTokenMember(String token) {
        return getParseData(token, Member.class);
    }

    /**
     * 인증 메일 코드 저장 (기존 인증 코드 삭제)
     *
     * @param email 인증 메일
     * @param code  인증 코드
     */
    public void saveCertMailCode(String email, String code) {
        if (Objects.isNull(email) || Objects.isNull(code)) {
            return;
        }

        deleteValues(email);

        final ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(email, code, Duration.ofDays(certMailExpired));
    }

    /**
     * 인증 메일 조회, 유효하지 않을 시 예외 처리
     *
     * @param code 인증 코드
     */
    public String getCertMailThrowIfInvalid(String code) {
        Assert.notNull(code, ErrorMessage.INVALID_PARAM.name());

        try {
            final String mail = EncryptionUtil.decode(code);

            final ValueOperations<String, String> values = redisTemplate.opsForValue();
            final String certMailCode = values.get(mail);

            Assert.isTrue(StringUtils.equals(code, certMailCode), ErrorMessage.INVALID_CERT_CODE.name());
            return mail;
        } catch (Exception e) {
            throw new CustomException(ErrorMessage.INVALID_CERT_CODE);
        }
    }

    /**
     * 메일 변경 정보 저장
     *
     * @param accessToken JWT 토큰
     * @param certInfoDto 인증 정보 객체
     */
    public void saveChangeMailInfo(String accessToken, CertInfoDto certInfoDto) {
        if (Objects.isNull(accessToken) || Objects.isNull(certInfoDto)) {
            return;
        }

        final String changeMailKey = CHANGE_MAIL_PREFIX + accessToken;
        final String jsonData = parserJacksonString(certInfoDto);

        deleteValues(changeMailKey);

        final ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(changeMailKey, jsonData, Duration.ofHours(certMailExpired));
    }

    /**
     * 메일 변경 정보 조회
     *
     * @param accessToken JWT 토큰
     */
    public Optional<CertInfoDto> getChangeMailInfo(String accessToken) {
        final String changeMailKey = CHANGE_MAIL_PREFIX + accessToken;
        return getParseData(changeMailKey, CertInfoDto.class);
    }

    /* 파싱한 데이터 조회 */
    private <T> Optional<T> getParseData(String key, Class<T> valueType) {
        final ValueOperations<String, String> values = redisTemplate.opsForValue();
        final String content = values.get(key);

        if (StringUtils.isEmpty(content)) {
            return Optional.empty();
        }

        return parseJsonToData(content, valueType);
    }

    /* Json 데이터 변환 */
    private String parserJacksonString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new CustomException(ErrorMessage.FAIL_PARSING);
        }
    }

    /* json 데이터 객체 변환 */
    private <T> Optional<T> parseJsonToData(String content, Class<T> valueType) {
        try {
            return Optional.of(
                    objectMapper.readValue(content, valueType));
        } catch (JsonProcessingException e) {
            log.error(ExceptionUtils.getStackTrace(e));
            log.error(JSON_PARSING_ERROR);
            return Optional.empty();
        }
    }

    /**
     * 레디스 값 삭제 여부 반환
     *
     * @param key 레디스 키
     */
    public boolean deleteValues(String key) {
        if (StringUtils.isEmpty(key)) {
            return false;
        }

        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }

}
