package com.jinwon.rpm.profile.domain.profile.dto;

import com.jinwon.rpm.profile.constants.enums.UseType;
import com.jinwon.rpm.profile.domain.terms.dto.TermsDetailDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "프로필 동의 조회 DTO")
public record ProfileAgreementDto(
        @Schema(description = "약관 상세 DTO") TermsDetailDto termsDetailDto,
        @Schema(description = "동의 여부") UseType agreeType) {
}
