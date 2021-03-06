package com.jinwon.rpm.profile.domain.terms_agreement;

import com.jinwon.rpm.profile.constants.enums.ErrorMessage;
import com.jinwon.rpm.profile.constants.enums.TermsType;
import com.jinwon.rpm.profile.constants.enums.UseType;
import com.jinwon.rpm.profile.domain.member.Member;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * 약관 동의 관련 서비스
 */
@Service
@Transactional
@RequiredArgsConstructor
public class TermsAgreementService {

    private final TermsService termsService;

    private final TermsAgreementRepository termsAgreementRepository;

    /**
     * 사용자 동의 정보 삭제
     *
     * @param member 사용자
     */
    public void deleteByMember(Member member) {
        Assert.notNull(member, ErrorMessage.NOT_EXIST_MEMBER.name());

        termsAgreementRepository.deleteByMember(member);
    }

    /**
     * 약관 동의 상세 조회
     *
     * @param member 사용자
     * @param type   약관 타입
     */
    public TermsAgreementDetailDto getTermsAgreementDetail(Member member, TermsType type) {
        Assert.notNull(type, ErrorMessage.NOT_EXIST_TERMS.name());
        Assert.notNull(member, ErrorMessage.NOT_EXIST_MEMBER.name());

        final Terms terms = termsService.getTermsByType(type);
        final PutTermsAgreementDto putTermsAgreementDto = new PutTermsAgreementDto(member, terms);

        final TermsDetailDto termsDetailDto = TermsDetailDto.of(terms);

        final Optional<TermsAgreement> termsAgreementOp = getTermsAgreement(putTermsAgreementDto);

        final UseType agreeType = termsAgreementOp.map(TermsAgreement::getAgreeType)
                .orElse(UseType.UNUSED);

        final LocalDateTime agreeDateTime = termsAgreementOp.map(TermsAgreement::getLastModifiedDateTime)
                .orElse(null);

        return new TermsAgreementDetailDto(termsDetailDto, agreeType, agreeDateTime);
    }

    /**
     * 기본 약관 동의 리스트 저장
     *
     * @param member          사용자
     * @param agreeTermsTypes 동의 약관 타입 리스트
     */
    public void putDefaultTermsAgreements(Member member, List<TermsType> agreeTermsTypes) {
        Assert.notNull(member, ErrorMessage.INVALID_PARAM.name());
        Assert.notNull(agreeTermsTypes, ErrorMessage.INVALID_PARAM.name());

        final List<Terms> defaultTerms = termsService.getDefaultTerms();

        final List<TermsAgreement> termsAgreements = defaultTerms.stream()
                .filter(Objects::nonNull)
                .map(terms -> getTermsAgreement(member, terms, agreeTermsTypes))
                .toList();

        termsAgreementRepository.saveAll(termsAgreements);
    }

    /* 약관 동의 생성 및 조회 */
    private TermsAgreement getTermsAgreement(Member member, Terms terms, List<TermsType> agreeTermsTypes) {
        final TermsType type = terms.getType();

        final UseType agreeType = Optional.ofNullable(type)
                .filter(agreeTermsTypes::contains)
                .map(termsType -> UseType.USE)
                .orElse(UseType.UNUSED);

        final Function<PutTermsAgreementDto, TermsAgreement> function = agreeType.createFunction();

        final PutTermsAgreementDto putTermsAgreementDto = new PutTermsAgreementDto(member, terms);
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
        final Member member = termsAgreementDto.member();
        final TermsType type = termsAgreementDto.type();

        final Terms terms = termsService.getTermsByType(type);
        return new PutTermsAgreementDto(member, terms);
    }

    /* 약관 동의 조회 */
    private Optional<TermsAgreement> getTermsAgreement(PutTermsAgreementDto putTermsAgreementDto) {
        final Member member = putTermsAgreementDto.member();
        final Terms terms = putTermsAgreementDto.terms();

        return termsAgreementRepository.findByMemberAndTerms(member, terms);
    }

    /* 약관 동의 저장 */
    private void saveTermsAgreement(PutTermsAgreementDto putTermsAgreementDto,
                                    Function<PutTermsAgreementDto, TermsAgreement> function) {
        final TermsAgreement termsAgreement = function.apply(putTermsAgreementDto);
        termsAgreementRepository.save(termsAgreement);
    }

}

