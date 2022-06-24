package com.jinwon.rpm.profile.constants.enums;

import lombok.Getter;

@Getter
public enum WithdrawType {

    /* 불 필요한 서비스 */
    UNNECESSARY_SERVICE,

    /* 잦은 메일로 인한 불편 */
    FREQUENT_EMAILS,

    /* 느리고 불 친절한 고객 대응 */
    UNFRIENDLY_CUSTOMER_RESPONSE,

    /* 낮은 사용 빈도 */
    LOW_FREQUENCY_OF_USE,

    /* 잦은 서비스 장애 */
    FREQUENT_SERVICE_DISRUPTIONS,

    /* 마이다스 프로그램 미사용 */
    MIDAS_PROGRAM_NOT_USED,

    /* 아이디, 이름 변경 */
    ID_OR_NAME_CHANGE,

    /* 기타 사유 */
    ETC,

}
