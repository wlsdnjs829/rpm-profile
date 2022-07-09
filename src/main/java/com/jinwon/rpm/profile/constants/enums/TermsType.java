package com.jinwon.rpm.profile.constants.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
@Schema(description = "약관 타입", enumAsRef = true)
public enum TermsType {

    /* MIDAS 통합 사용자 */
    UNIFIED_MIDAS_MEMBER,

    /* 개인 정보 수집 이용 안내 */
    PRIVACY_COLLECTION,

    /* 서비스 이용 약관 */
    TERMS_OF_SERVICE,

    /* 마케팅 정보 수신 동의 */
    RECEIVE_MARKETING_INFO;

    /**
     * 기본 약관 리스트 조회
     */
    public static List<TermsType> defaultTerms() {
        return List.of(UNIFIED_MIDAS_MEMBER, PRIVACY_COLLECTION, TERMS_OF_SERVICE, RECEIVE_MARKETING_INFO);
    }

}
