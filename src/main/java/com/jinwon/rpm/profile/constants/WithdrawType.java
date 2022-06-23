package com.jinwon.rpm.profile.constants;

import lombok.Getter;

@Getter
public enum WithdrawType {

    UNNECESSARY_SERVICE("불필요한 서비스"),
    FREQUENT_EMAILS("잦은 메일로 인한 불편"),
    UNFRIENDLY_CUSTOMER_RESPONSE("느리고 불친절한 고객대응"),
    LOW_FREQUENCY_OF_USE("낮은 사용 빈도"),
    FREQUENT_SERVICE_DISRUPTIONS("잦은 서비스 장애"),
    MIDAS_PROGRAM_NOT_USED("마이다스 프로그램 미사용"),
    ID_OR_NAME_CHANGE("아이디, 이름 변경"),
    ETC("기타 사유");

    private final String name;

    WithdrawType(String name) {
        this.name = name;
    }

}
