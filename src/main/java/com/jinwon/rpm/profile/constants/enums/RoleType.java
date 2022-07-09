package com.jinwon.rpm.profile.constants.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "권한 타입", enumAsRef = true)
public enum RoleType {

    USER,
    ADMIN,

}
