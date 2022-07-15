package com.jinwon.rpm.profile.constants.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "메일 발송 상태", enumAsRef = true)
public enum SendStatus {

    SUCCESS,
    FAIL,

}
