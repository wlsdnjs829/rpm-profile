package com.jinwon.rpm.profile.constants.enums;

import com.jinwon.rpm.profile.domain.terms_agreement.TermsAgreement;
import com.jinwon.rpm.profile.domain.terms_agreement.inner_dto.PutTermsAgreementDto;

import java.util.function.Function;

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

interface TermsAgreementFn {

    Function<PutTermsAgreementDto, TermsAgreement> createFunction();

}
