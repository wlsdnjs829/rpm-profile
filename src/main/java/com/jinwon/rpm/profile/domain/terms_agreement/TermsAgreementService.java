package com.jinwon.rpm.profile.domain.terms_agreement;

import com.jinwon.rpm.profile.constants.ErrorMessage;
import com.jinwon.rpm.profile.constants.enums.UseType;
import com.jinwon.rpm.profile.domain.profile.Profile;
import com.jinwon.rpm.profile.domain.terms.Terms;
import com.jinwon.rpm.profile.domain.terms_agreement.inner_dto.PutTermsAgreementDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.internal.util.Assert;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.function.Function;

@Service
@Transactional
@RequiredArgsConstructor
public class TermsAgreementService {

    private final TermsAgreementRepository termsAgreementRepository;

    /**
     * 프로필 동의 정보 삭제
     *
     * @param profile 프로필
     */
    public void deleteByProfile(@NotNull Profile profile) {
        Assert.notNull(profile, ErrorMessage.NOT_EXIST_PROFILE.name());

        termsAgreementRepository.deleteByProfile(profile);
    }

    /**
     * 약관 동의 타입 조회
     *
     * @param profile 프로필
     * @param terms   약관
     */
    public UseType getTermsAgreementAgreeType(@NotNull Profile profile, @NotNull Terms terms) {
        Assert.notNull(terms, ErrorMessage.NOT_EXIST_TERMS.name());
        Assert.notNull(profile, ErrorMessage.NOT_EXIST_PROFILE.name());

        return getTermsAgreement(profile, terms)
                .map(TermsAgreement::getAgreeType)
                .orElse(UseType.UNUSED);
    }

    /**
     * 약관 동의 생성 / 수정
     *
     * @param putTermsAgreementDto 약관 동의 생성 DTO
     * @param agreeType            동의 타입
     */
    public void putTermsAgreement(@NotNull PutTermsAgreementDto putTermsAgreementDto, @NotNull UseType agreeType) {
        Assert.notNull(agreeType, ErrorMessage.INVALID_PARAM.name());
        Assert.notNull(putTermsAgreementDto, ErrorMessage.INVALID_PARAM.name());

        final Profile profile = putTermsAgreementDto.profile();
        final Terms terms = putTermsAgreementDto.terms();

        final Function<PutTermsAgreementDto, TermsAgreement> function = agreeType.createFunction();

        getTermsAgreement(profile, terms)
                .ifPresentOrElse(termsAgreement -> termsAgreement.agreeOrNot(agreeType),
                        () -> {
                            final TermsAgreement termsAgreement = function.apply(putTermsAgreementDto);
                            termsAgreementRepository.save(termsAgreement);
                        });
    }

    /* 약관 동의 조회 */
    private Optional<TermsAgreement> getTermsAgreement(@NotNull Profile profile, @NotNull Terms terms) {
        return termsAgreementRepository.findByProfileAndTerms(profile, terms);
    }

}

