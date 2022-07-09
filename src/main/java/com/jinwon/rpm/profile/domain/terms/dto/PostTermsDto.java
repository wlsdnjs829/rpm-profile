package com.jinwon.rpm.profile.domain.terms.dto;

import com.jinwon.rpm.profile.constants.enums.TermsCondition;
import com.jinwon.rpm.profile.constants.enums.TermsType;
import com.jinwon.rpm.profile.constants.enums.UseType;
import com.jinwon.rpm.profile.domain.terms.Terms;
import com.jinwon.rpm.profile.infra.utils.ModelMapperUtil;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Schema(description = "약관 등록 DTO")
public record PostTermsDto(
        @NotNull @Schema(description = "약관 타입") TermsType type,
        @NotNull @Schema(description = "약관 종류") TermsCondition termsCondition,
        @NotNull @Schema(description = "약관 제목") String title,
        @Schema(description = "약관 내용") String contents,
        @NotNull @Schema(description = "약관 사용 타입") UseType useType,
        @NotNull @Schema(description = "약관 버전") String version,
        @NotNull @Schema(description = "약관 공고일") LocalDateTime noticeDateTime,
        @NotNull @Schema(description = "약관 시행일") LocalDateTime effectiveDateTime) {

    public Terms toEntity() {
        return ModelMapperUtil.map(this, Terms.class);
    }

}
