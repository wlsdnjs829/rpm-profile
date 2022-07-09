package com.jinwon.rpm.profile.constants.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "약관 종류", enumAsRef = true)
public enum TermsCondition {

    /* 필수 */
    ESSENTIAL,

    /* 안내 */
    GUIDE,

    /* 선택 */
    OPTIONAL,

}
