package com.jinwon.rpm.profile.domain.terms_agreement.inner_dto;

import com.jinwon.rpm.profile.constants.enums.TermsType;
import com.jinwon.rpm.profile.constants.enums.UseType;
import com.jinwon.rpm.profile.domain.profile.Profile;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "약관 동의 DTO")
public record TermsAgreementDto(
        @Schema(description = "프로필") Profile profile,
        @Schema(description = "약관 타입") TermsType type,
        @Schema(description = "동의 여부") UseType agreeType) {
}
