package com.jinwon.rpm.profile.domain.terms_agreement.dto;

import com.jinwon.rpm.profile.constants.enums.UseType;
import com.jinwon.rpm.profile.domain.terms.dto.TermsDetailDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "약관 동의 상세 조회 DTO")
public record TermsAgreementDetailDto(
        @Schema(description = "약관 상세 DTO") TermsDetailDto termsDetail,
        @Schema(description = "동의 여부") UseType agreeType,
        @Schema(description = "동의 날짜") LocalDateTime agreeDateTime) {

    public TermsAgreementDetailDto(TermsDetailDto termsDetail, UseType agreeType) {
        this(termsDetail, agreeType, LocalDateTime.now());
    }

}
