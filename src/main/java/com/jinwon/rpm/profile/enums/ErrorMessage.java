package com.jinwon.rpm.profile.enums;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

/**
 * Client Error Message Enum
 */
@Getter
public enum ErrorMessage {

    NOT_EXIST_PROFILE(HttpStatus.FORBIDDEN, "존재 하지 않는 프로필"),
    INVALID_EMAIL(HttpStatus.BAD_REQUEST, "적합하지 않은 이메일"),

    MISMATCH_PASSWORD(HttpStatus.BAD_REQUEST, "패스워드 불일치"),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "적합하지 않은 패스워드"),
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
