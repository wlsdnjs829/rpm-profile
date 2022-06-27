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

    NOT_EXIST_TERMS(HttpStatus.BAD_REQUEST, "E001", "존재하지 않는 약관"),
    NOT_EXIST_PROFILE(HttpStatus.FORBIDDEN, "E002", "존재하지 않는 프로필"),
    NOT_EXIST_ATTACH_FILE(HttpStatus.BAD_REQUEST, "E003", "존재하지 않는 파일"),

    EXIST_TERMS(HttpStatus.BAD_REQUEST, "E004", "존재하는 약관"),
    EXIST_EMAIL(HttpStatus.BAD_REQUEST, "E005", "존재하는 이메일"),

    INVALID_PARAM(HttpStatus.BAD_REQUEST, "E201", "적합하지 않은 파라미터"),
    INVALID_EMAIL(HttpStatus.BAD_REQUEST, "E202", "적합하지 않은 이메일"),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "E203", "적합하지 않은 패스워드"),
    INVALID_TERMS_TYPE(HttpStatus.BAD_REQUEST, "E204", "적합하지 않은 약관 타입"),
    INVALID_FILE_EXTENSION(HttpStatus.BAD_REQUEST, "E205", "적합하지 않은 파일 확장자명"),

    FAIL_S3_UPLOAD(HttpStatus.INTERNAL_SERVER_ERROR, "E501", "파일 업로드 실패"),
    FAIL_S3_DOWNLOAD(HttpStatus.INTERNAL_SERVER_ERROR, "E502", "파일 다운로드 실패"),
    FAIL_POST_WITHDRAW(HttpStatus.INTERNAL_SERVER_ERROR, "E503", "회원 탈퇴 사유 생성 실패"),

    SAME_AGREE_TYPE(HttpStatus.BAD_REQUEST, "E901", "같은 동의 타입"),
    MISMATCH_PASSWORD(HttpStatus.BAD_REQUEST, "E902", "패스워드 불일치"),
    NOT_USE_OLD_PASSWORD(HttpStatus.BAD_REQUEST, "E903", "기존 패스워드 사용 불가"),
    DONT_MEET_TERMS_OF_CONSENT(HttpStatus.BAD_REQUEST, "E904", "동의 조건 미충족"),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ErrorMessage(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
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
