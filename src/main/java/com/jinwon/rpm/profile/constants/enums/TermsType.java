package com.jinwon.rpm.profile.constants.enums;

import lombok.Getter;

import java.util.List;

/**
 * 약관 타입
 */
@Getter
public enum TermsType {

    /* MIDAS 통합 프로필 */
    UNIFIED_MIDAS_PROFILE,

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
        return List.of(UNIFIED_MIDAS_PROFILE, PRIVACY_COLLECTION, TERMS_OF_SERVICE, RECEIVE_MARKETING_INFO);
    }

}
