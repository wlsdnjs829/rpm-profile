package com.jinwon.rpm.profile.domain.terms;

import com.jinwon.rpm.profile.constants.enums.ErrorMessage;
import com.jinwon.rpm.profile.constants.enums.TermsType;
import com.jinwon.rpm.profile.domain.terms.dto.PostTermsDto;
import com.jinwon.rpm.profile.domain.terms.dto.TermsDefaultDto;
import com.jinwon.rpm.profile.domain.terms.dto.TermsDetailDto;
import com.jinwon.rpm.profile.domain.terms.dto.TermsDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.internal.util.Assert;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 약관 서비스
 */
@Service
@Transactional
@RequiredArgsConstructor
public class TermsService {

    private final TermsRepository termsRepository;
    private final TermsRepositoryImpl termsRepositoryImpl;

    /**
     * 기본 약관 DTO 리스트 조회
     */
    public TermsDefaultDto getDefaultTermsDto() {
        final List<Terms> defaultTerms = getDefaultTerms();

        final List<TermsDto> termsDtoList =
                defaultTerms.stream()
                        .map(TermsDto::of)
                        .toList();

        return new TermsDefaultDto(termsDtoList);
    }

    /**
     * 기본 약관 리스트 조회
     */
    public List<Terms> getDefaultTerms() {
        final List<TermsType> termsTypes = TermsType.defaultTerms();
        return termsRepositoryImpl.findListUseTerms(termsTypes);
    }

    /**
     * 약관 상세 정보 조회
     *
     * @param type 약관 타입
     */
    public TermsDetailDto getTermsDetail(TermsType type) {
        Assert.notNull(type, ErrorMessage.INVALID_TERMS_TYPE.name());

        final Terms terms = termsRepositoryImpl.findUseTermsByType(type);
        return TermsDetailDto.of(terms);
    }

    /**
     * 약관 저장
     *
     * @param postTermsDto 약관 저장 DTO
     * @return 저장된 약관 상세 정보
     */
    public TermsDetailDto postTerms(PostTermsDto postTermsDto) {
        Assert.notNull(postTermsDto, ErrorMessage.INVALID_PARAM.name());

        final Optional<Terms> existTerms =
                termsRepository.findByTypeAndVersion(postTermsDto.type(), postTermsDto.version());

        Assert.isTrue(existTerms.isEmpty(), ErrorMessage.EXIST_TERMS.name());

        final Terms terms = postTermsDto.toEntity();
        final Terms saveTerms = termsRepository.save(terms);
        return TermsDetailDto.of(saveTerms);
    }

    /**
     * 타입에 맞는 약관 조회
     *
     * @param type 약관 타입
     */
    public Terms getTermsByType(TermsType type) {
        Assert.notNull(type, ErrorMessage.INVALID_PARAM.name());
        return termsRepositoryImpl.findUseTermsByType(type);
    }

}

