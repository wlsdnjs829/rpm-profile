package com.jinwon.rpm.profile.constants.enums;

import com.jinwon.rpm.profile.domain.terms_agreement.TermsAgreement;
import com.jinwon.rpm.profile.domain.terms_agreement.inner_dto.PutTermsAgreementDto;

import java.util.function.Function;

/**
 * 사용 타입
 */
public enum UseType implements TermsAgreementFn {

    USE {

        @Override
        public Function<PutTermsAgreementDto, TermsAgreement> createFunction() {
            return TermsAgreement::agreeTermsAgreement;
        }

    },

    UNUSED {

        @Override
        public Function<PutTermsAgreementDto, TermsAgreement> createFunction() {
            return TermsAgreement::disagreeTermsAgreement;
        }

    },

}

/**
 * 약관 동의 펑션
 */
interface TermsAgreementFn {

    Function<PutTermsAgreementDto, TermsAgreement> createFunction();

}
