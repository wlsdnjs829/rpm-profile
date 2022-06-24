package com.jinwon.rpm.profile.domain.terms.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "기본 약관 DTO")
public record TermsDefaultDto(
        @Schema(description = "약관 리스트") List<TermsDto> termsDtoList) {
}
