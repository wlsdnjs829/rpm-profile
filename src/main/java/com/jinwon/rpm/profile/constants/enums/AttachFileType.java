package com.jinwon.rpm.profile.constants.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "첨부 파일 타입", enumAsRef = true)
public enum AttachFileType {

    MEMBER("/member"),
    ;

    private final String path;

    AttachFileType(String path) {
        this.path = path;
    }

}
