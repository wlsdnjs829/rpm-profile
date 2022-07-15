package com.jinwon.rpm.profile.constants.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

@Getter
@Schema(description = "에러 메시지", enumAsRef = true)
public enum ErrorMessage {

    NOT_EXIST_TERMS(HttpStatus.BAD_REQUEST, "존재하지 않는 약관"),
    NOT_EXIST_MEMBER(HttpStatus.FORBIDDEN, "존재하지 않는 사용자"),
    NOT_EXIST_ATTACH_FILE(HttpStatus.BAD_REQUEST, "존재하지 않는 파일"),

    EXIST_TERMS(HttpStatus.BAD_REQUEST, "존재하는 약관"),
    EXIST_EMAIL(HttpStatus.BAD_REQUEST, "존재하는 이메일"),

    INVALID_EMAIL(HttpStatus.BAD_REQUEST, "적합하지 않은 이메일"),
    INVALID_PARAM(HttpStatus.BAD_REQUEST, "적합하지 않은 파라미터"),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "적합하지 않은 패스워드"),
    INVALID_CERT_CODE(HttpStatus.BAD_REQUEST, "적합하지 않은 인증 코드"),
    INVALID_TERMS_TYPE(HttpStatus.BAD_REQUEST, "적합하지 않은 약관 타입"),
    INVALID_SUPPLIER(HttpStatus.INTERNAL_SERVER_ERROR, "SUPPLIER 오류"),
    INVALID_FILE_EXTENSION(HttpStatus.BAD_REQUEST, "적합하지 않은 파일 확장자명"),

    DISABLE_MEMBER(HttpStatus.FORBIDDEN, "비활성화된 사용자"),
    LOCKED_MEMBER(HttpStatus.FORBIDDEN, "잠겨 있는 사용자"),

    FAIL_PARSING(HttpStatus.INTERNAL_SERVER_ERROR, "파싱 실패"),
    FAIL_SEND_MAIL(HttpStatus.INTERNAL_SERVER_ERROR, "메일 발송 실패"),
    FAIL_S3_UPLOAD(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드 실패"),
    FAIL_S3_DOWNLOAD(HttpStatus.INTERNAL_SERVER_ERROR, "파일 다운로드 실패"),
    FAIL_POST_WITHDRAW(HttpStatus.INTERNAL_SERVER_ERROR, "사용자 탈퇴 사유 생성 실패"),

    SAME_AGREE_TYPE(HttpStatus.BAD_REQUEST, "같은 동의 타입"),
    MISMATCH_PASSWORD(HttpStatus.BAD_REQUEST, "패스워드 불일치"),
    NOT_USE_OLD_PASSWORD(HttpStatus.BAD_REQUEST, "기존 패스워드 사용 불가"),
    DONT_MEET_TERMS_OF_CONSENT(HttpStatus.BAD_REQUEST, "동의 조건 미충족"),

    JWT_SIGNATURE(HttpStatus.FORBIDDEN, "유효하지 않은 시그니쳐"),
    JWT_MALFORMED(HttpStatus.FORBIDDEN, "유효하지 않은 JWT"),
    JWT_EXPIRED(HttpStatus.FORBIDDEN, "만료된 JWT"),
    JWT_UNSUPPORTED(HttpStatus.FORBIDDEN, "지원하지 않는 JWT"),
    JWT_ILLEGAL(HttpStatus.BAD_REQUEST, "존재하지 않는 JWT 내부 정보"),
    JWT_NON_EXPIRED(HttpStatus.BAD_REQUEST, "만료되지 않은 JWT"),
    JWT_MALFORMED_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않은 JWT"),
    JWT_EXPIRED_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "만료된 REFRASH TOKEN"),
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