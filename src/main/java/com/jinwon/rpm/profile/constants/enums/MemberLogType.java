package com.jinwon.rpm.profile.constants.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 로그 타입", enumAsRef = true)
public enum MemberLogType {

    LOGIN,
    REFRESH_TOKEN,
    LOGOUT,
    FAIL,
    LOCK,

}
