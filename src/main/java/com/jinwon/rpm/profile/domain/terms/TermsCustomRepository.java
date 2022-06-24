package com.jinwon.rpm.profile.domain.terms;

import com.jinwon.rpm.profile.constants.enums.TermsType;

import java.util.List;

/**
 * 약관 커스텀 Repository
 */
public interface TermsCustomRepository {

    Terms findUseTermsByType(TermsType termsType);

    List<Terms> findListUseTerms(List<TermsType> termsTypes);

}