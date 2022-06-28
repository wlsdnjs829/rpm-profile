package com.jinwon.rpm.profile.constants.enums;

import lombok.Getter;

/**
 * 첨부 파일 타입
 */
@Getter
public enum AttachFileType {

    PROFILE("/profile"),
    ;

    private final String path;

    AttachFileType(String path) {
        this.path = path;
    }

}
