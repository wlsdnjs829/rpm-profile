package com.jinwon.rpm.profile.domain.profile;

import com.jinwon.rpm.profile.constants.ErrorMessage;
import com.jinwon.rpm.profile.constants.enums.TermsType;
import com.jinwon.rpm.profile.constants.enums.UseType;
import com.jinwon.rpm.profile.domain.profile.dto.ProfileAgreementDto;
import com.jinwon.rpm.profile.domain.terms.Terms;
import com.jinwon.rpm.profile.domain.terms.TermsAgreement;
import com.jinwon.rpm.profile.domain.terms.TermsAgreementRepository;
import com.jinwon.rpm.profile.domain.terms.TermsRepositoryImpl;
import com.jinwon.rpm.profile.domain.terms.dto.TermsDetailDto;
import com.jinwon.rpm.profile.infra.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.internal.util.Assert;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;

@Service
@Transactional
@RequiredArgsConstructor
public class ProfileAgreementService {

    private final ProfileRepository profileRepository;
    private final TermsRepositoryImpl termsRepositoryImpl;
    private final TermsAgreementRepository termsAgreementRepository;

    /**
     * 프로필 동의 여부 조회
     *
     * @param profileId 프로필 아이디
     * @param type      동의서 타입
     */
    public ProfileAgreementDto getProfileAgreement(@NotNull Long profileId, @NotNull TermsType type) {
        Assert.notNull(profileId, ErrorMessage.INVALID_PARAM.name());
        Assert.notNull(type, ErrorMessage.INVALID_PARAM.name());

        final Profile profile = getProfileThrowIfNull(profileId);

        final Terms terms = termsRepositoryImpl.findUseTermsByType(type);

        final UseType agreeType = termsAgreementRepository.findByProfileAndTerms(profile, terms)
                .map(TermsAgreement::getAgreeType)
                .orElse(UseType.UNUSED);

        final TermsDetailDto termsDetailDto = TermsDetailDto.of(terms);
        return new ProfileAgreementDto(termsDetailDto, agreeType);
    }

    /**
     * 프로필 마케팅 동의 생성 / 수정
     *
     * @param profileId 프로필 아이디
     * @param agreeType 동의 타입
     * @return 동의한 프로필 DTO
     */
    public ProfileAgreementDto putProfileMarketingAgree(@NotNull Long profileId, @NotNull UseType agreeType) {
        Assert.notNull(profileId, ErrorMessage.INVALID_PARAM.name());
        Assert.notNull(agreeType, ErrorMessage.INVALID_PARAM.name());

        final Profile profile = getProfileThrowIfNull(profileId);

        final Terms terms = termsRepositoryImpl.findUseTermsByType(TermsType.RECEIVE_MARKETING_INFO);

        termsAgreementRepository.findByProfileAndTerms(profile, terms)
                .ifPresentOrElse(termsAgreement -> termsAgreement.agreeOrNot(agreeType),
                        () -> termsAgreementRepository.save(TermsAgreement.create(profile, terms, agreeType)));

        final TermsDetailDto termsDetailDto = TermsDetailDto.of(terms);
        return new ProfileAgreementDto(termsDetailDto, agreeType);
    }

    /* 프로필 조회, 없을 시 예외 처리 */
    private Profile getProfileThrowIfNull(@NotNull Long profileId) {
        return profileRepository.findById(profileId)
                .orElseThrow(() -> new CustomException(ErrorMessage.NOT_EXIST_PROFILE));
    }

}

