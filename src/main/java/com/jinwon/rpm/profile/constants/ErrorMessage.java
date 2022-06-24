package com.jinwon.rpm.profile.constants;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

/**
 * Client Error Message Enum
 */
@Getter
public enum ErrorMessage {

    NOT_EXIST_PROFILE(HttpStatus.FORBIDDEN, "존재하지 않는 프로필"),
    EXIST_EMAIL(HttpStatus.BAD_REQUEST, "존재하는 이메일"),
    INVALID_EMAIL(HttpStatus.BAD_REQUEST, "적합하지 않은 이메일"),

    MISMATCH_PASSWORD(HttpStatus.BAD_REQUEST, "패스워드 불일치"),
    NOT_USE_OLD_PASSWORD(HttpStatus.BAD_REQUEST, "기존 패스워드 사용 불가"),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "적합하지 않은 패스워드"),

    FAIL_POST_WITHDRAW(HttpStatus.INTERNAL_SERVER_ERROR, "회원 탈퇴 사유 생성 실패"),

    DONT_MEET_TERMS_OF_CONSENT(HttpStatus.BAD_REQUEST, "동의 조건 미충족"),
    INVALID_TERMS_TYPE(HttpStatus.BAD_REQUEST, "적합하지 않은 약관 타입"),
    NOT_EXIST_TERMS(HttpStatus.BAD_REQUEST, "존재하지 않는 약관"),
    SAME_AGREE_TYPE(HttpStatus.BAD_REQUEST, "같은 동의 타입"),
    EXIST_TERMS(HttpStatus.BAD_REQUEST, "존재하는 약관"),

    INVALID_PARAM(HttpStatus.BAD_REQUEST, "적합하지 않은 파라미터"),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    ErrorMessage(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public static ErrorMessage getErrorMessageByName(String name) {
        if (StringUtils.isEmpty(name)) {
            return null;
        }

        return Arrays.stream(ErrorMessage.values())
                .filter(errorMessage -> errorMessage.name().equals(name))
                .findFirst()
                .orElse(null);
    }

}
