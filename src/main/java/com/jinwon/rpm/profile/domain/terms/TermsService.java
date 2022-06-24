package com.jinwon.rpm.profile.domain.terms;

import com.jinwon.rpm.profile.constants.ErrorMessage;
import com.jinwon.rpm.profile.constants.enums.TermsType;
import com.jinwon.rpm.profile.domain.terms.dto.PostTermsDto;
import com.jinwon.rpm.profile.domain.terms.dto.TermsDefaultDto;
import com.jinwon.rpm.profile.domain.terms.dto.TermsDetailDto;
import com.jinwon.rpm.profile.domain.terms.dto.TermsDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.internal.util.Assert;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TermsService {

    private final TermsRepository termsRepository;
    private final TermsRepositoryImpl termsRepositoryImpl;

    /**
     * 기본 약관 리스트 조회
     */
    public TermsDefaultDto getDefaultTerms() {
        final List<TermsType> termsTypes = TermsType.defaultTerms();

        final List<TermsDto> termsDtoList = termsRepositoryImpl.findListUseTerms(termsTypes)
                .stream()
                .map(TermsDto::of)
                .toList();

        return new TermsDefaultDto(termsDtoList);
    }

    /**
     * 약관 상세 정보 조회
     *
     * @param type 약관 타입
     */
    public TermsDetailDto getTermsDetail(@NotNull TermsType type) {
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
    public TermsDetailDto postTerms(@NotNull PostTermsDto postTermsDto) {
        Assert.notNull(postTermsDto, ErrorMessage.INVALID_PARAM.name());

        final Optional<Terms> existTerms =
                termsRepository.findByTypeAndVersion(postTermsDto.type(), postTermsDto.version());

        Assert.isTrue(existTerms.isEmpty(), ErrorMessage.EXIST_TERMS.name());

        final Terms terms = postTermsDto.toEntity();
        final Terms saveTerms = termsRepository.save(terms);
        return TermsDetailDto.of(saveTerms);
    }

}

