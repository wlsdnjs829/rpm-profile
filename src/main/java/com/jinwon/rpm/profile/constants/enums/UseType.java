package com.jinwon.rpm.profile.constants.enums;

import com.jinwon.rpm.profile.domain.terms_agreement.TermsAgreement;
import com.jinwon.rpm.profile.domain.terms_agreement.inner_dto.PutTermsAgreementDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.function.Function;

@Schema(description = "사용 타입", enumAsRef = true)
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
