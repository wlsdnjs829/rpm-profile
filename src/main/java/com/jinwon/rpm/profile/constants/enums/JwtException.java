package com.jinwon.rpm.profile.constants.enums;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.Arrays;

@Getter
@Schema(description = "JWT Provider Exception Message Enum", enumAsRef = true)
public enum JwtException {

    SIGNATURE(SignatureException.class, ErrorMessage.JWT_SIGNATURE),
    MALFORMED(MalformedJwtException.class, ErrorMessage.JWT_MALFORMED),
    EXPIRED(ExpiredJwtException.class, ErrorMessage.JWT_EXPIRED),
    UNSUPPORTED(UnsupportedJwtException.class, ErrorMessage.JWT_UNSUPPORTED),
    ILLEGAL(IllegalArgumentException.class, ErrorMessage.JWT_ILLEGAL);

    private final Class<?> exceptionClass;
    private final ErrorMessage errorMessage;

    private static final String DEFAULT_ERROR_MASSAGE = "Jwt token provider error";

    JwtException(Class<?> exceptionClass, ErrorMessage errorMessage) {
        this.exceptionClass = exceptionClass;
        this.errorMessage = errorMessage;
    }

    public static String getMessageByExceptionClass(Class<?> exceptionClass) {
        return Arrays.stream(JwtException.values())
                .filter(e -> e.getExceptionClass().equals(exceptionClass))
                .map(JwtException::getErrorMessage)
                .map(ErrorMessage::getMessage)
                .findFirst()
                .orElse(DEFAULT_ERROR_MASSAGE);
    }

}
