package com.jinwon.rpm.profile.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Client Error Message Enum
 */
@Getter
public enum ErrorMessage {

    NOT_EXIST_PROFILE(HttpStatus.FORBIDDEN, "존재 하지 않는 프로필"),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    ErrorMessage(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

}
