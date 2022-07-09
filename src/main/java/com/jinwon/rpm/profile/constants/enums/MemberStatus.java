package com.jinwon.rpm.profile.constants.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 상태", enumAsRef = true)
public enum MemberStatus {

    ACTIVATE,
    DISABLED,
    LOCKED,
    SLEEP,

}
