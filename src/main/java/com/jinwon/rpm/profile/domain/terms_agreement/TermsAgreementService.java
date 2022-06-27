package com.jinwon.rpm.profile.domain.terms_agreement;

import com.jinwon.rpm.profile.constants.ErrorMessage;
import com.jinwon.rpm.profile.constants.enums.TermsType;
import com.jinwon.rpm.profile.constants.enums.UseType;
import com.jinwon.rpm.profile.domain.profile.Profile;
import com.jinwon.rpm.profile.domain.terms.Terms;
import com.jinwon.rpm.profile.domain.terms.TermsService;
import com.jinwon.rpm.profile.domain.terms.dto.TermsDetailDto;
import com.jinwon.rpm.profile.domain.terms_agreement.dto.TermsAgreementDetailDto;
import com.jinwon.rpm.profile.domain.terms_agreement.inner_dto.PutTermsAgreementDto;
import com.jinwon.rpm.profile.domain.terms_agreement.inner_dto.TermsAgreementDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.internal.util.Assert;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

@Service
@Transactional
@RequiredArgsConstructor
public class TermsAgreementService {

    private final TermsService termsService;

    private final TermsAgreementRepository termsAgreementRepository;

    /**
     * 프로필 동의 정보 삭제
     *
     * @param profile 프로필
     */
    public void deleteByProfile(Profile profile) {
        Assert.notNull(profile, ErrorMessage.NOT_EXIST_PROFILE.name());

        termsAgreementRepository.deleteByProfile(profile);
    }

    /**
     * 약관 동의 상세 조회
     *
     * @param profile 프로필
     * @param type    약관 타입
     */
    public TermsAgreementDetailDto getTermsAgreementDetail(Profile profile, TermsType type) {
        Assert.notNull(type, ErrorMessage.NOT_EXIST_TERMS.name());
        Assert.notNull(profile, ErrorMessage.NOT_EXIST_PROFILE.name());

        final Terms terms = termsService.getTermsByType(type);
        final PutTermsAgreementDto putTermsAgreementDto = new PutTermsAgreementDto(profile, terms);

        final UseType agreeType = getTermsAgreement(putTermsAgreementDto)
                .map(TermsAgreement::getAgreeType)
                .orElse(UseType.UNUSED);

        final TermsDetailDto termsDetailDto = TermsDetailDto.of(terms);
        return new TermsAgreementDetailDto(termsDetailDto, agreeType);
    }

    /**
     * 기본 약관 동의 리스트 저장
     *
     * @param profile         프로필
     * @param agreeTermsTypes 동의 약관 타입 리스트
     */
    public void putDefaultTermsAgreements(Profile profile, List<TermsType> agreeTermsTypes) {
        Assert.notNull(profile, ErrorMessage.INVALID_PARAM.name());
        Assert.notNull(agreeTermsTypes, ErrorMessage.INVALID_PARAM.name());

        final List<Terms> defaultTerms = termsService.getDefaultTerms();

        final List<TermsAgreement> termsAgreements = defaultTerms.stream()
                .filter(Objects::nonNull)
                .map(terms -> getTermsAgreement(profile, terms, agreeTermsTypes))
                .toList();

        termsAgreementRepository.saveAll(termsAgreements);
    }

    /* 약관 동의 생성 및 조회 */
    private TermsAgreement getTermsAgreement(Profile profile, Terms terms, List<TermsType> agreeTermsTypes) {
        final TermsType type = terms.getType();

        final UseType agreeType = Optional.ofNullable(type)
                .filter(agreeTermsTypes::contains)
                .map(termsType -> UseType.USE)
                .orElse(UseType.UNUSED);

        final Function<PutTermsAgreementDto, TermsAgreement> function = agreeType.createFunction();

        final PutTermsAgreementDto putTermsAgreementDto = new PutTermsAgreementDto(profile, terms);
        return function.apply(putTermsAgreementDto);
    }

    /**
     * 약관 동의 생성 / 수정
     *
     * @param termsAgreementDto 약관 동의 DTO
     * @return 약관 동의 상세 DTO
     */
    public TermsAgreementDetailDto putTermsAgreement(TermsAgreementDto termsAgreementDto) {
        Assert.notNull(termsAgreementDto, ErrorMessage.INVALID_PARAM.name());

        final PutTermsAgreementDto putTermsAgreementDto = getPutTermsAgreementDto(termsAgreementDto);

        final UseType agreeType = termsAgreementDto.agreeType();
        final Function<PutTermsAgreementDto, TermsAgreement> function = agreeType.createFunction();

        getTermsAgreement(putTermsAgreementDto)
                .ifPresentOrElse(termsAgreement -> termsAgreement.agreeOrNot(agreeType),
                        () -> saveTermsAgreement(putTermsAgreementDto, function));

        final Terms terms = putTermsAgreementDto.terms();
        final TermsDetailDto termsDetailDto = TermsDetailDto.of(terms);
        return new TermsAgreementDetailDto(termsDetailDto, agreeType);
    }

    /* 약관 동의 생성 DTO 조회 */
    private PutTermsAgreementDto getPutTermsAgreementDto(TermsAgreementDto termsAgreementDto) {
        final Profile profile = termsAgreementDto.profile();
        final TermsType type = termsAgreementDto.type();

        final Terms terms = termsService.getTermsByType(type);
        return new PutTermsAgreementDto(profile, terms);
    }

    /* 약관 동의 조회 */
    private Optional<TermsAgreement> getTermsAgreement(PutTermsAgreementDto putTermsAgreementDto) {
        final Profile profile = putTermsAgreementDto.profile();
        final Terms terms = putTermsAgreementDto.terms();

        return termsAgreementRepository.findByProfileAndTerms(profile, terms);
    }

    /* 약관 동의 저장 */
    private void saveTermsAgreement(PutTermsAgreementDto putTermsAgreementDto,
                                    Function<PutTermsAgreementDto, TermsAgreement> function) {
        final TermsAgreement termsAgreement = function.apply(putTermsAgreementDto);
        termsAgreementRepository.save(termsAgreement);
    }

}

