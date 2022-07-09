package com.jinwon.rpm.profile.domain.terms_agreement.inner_dto;

import com.jinwon.rpm.profile.domain.member.Member;
import com.jinwon.rpm.profile.domain.terms.Terms;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;

@Schema(description = "약관 동의 등록 DTO")
public record PutTermsAgreementDto(
        @NotNull Member member,
        @NotNull Terms terms) {
}
