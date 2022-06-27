package com.jinwon.rpm.profile.domain.terms_agreement.dto;

import com.jinwon.rpm.profile.constants.enums.UseType;
import com.jinwon.rpm.profile.domain.terms.dto.TermsDetailDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "약관 동의 상세 조회 DTO")
public record TermsAgreementDetailDto(
        @Schema(description = "약관 상세 DTO") TermsDetailDto termsDetail,
        @Schema(description = "동의 여부") UseType agreeType) {
}
