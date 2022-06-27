package com.jinwon.rpm.profile.domain.terms;

import com.jinwon.rpm.profile.constants.enums.TermsType;

import java.util.List;

/**
 * 약관 커스텀 Repository
 */
public interface TermsCustomRepository {

    /**
     * 사용 중인 약관 조회
     *
     * @param termsType 약관 타입
     */
    Terms findUseTermsByType(TermsType termsType);

    /**
     * 약관 리스트 조회
     *
     * @param termsTypes 약관 타입
     */
    List<Terms> findListUseTerms(List<TermsType> termsTypes);

}