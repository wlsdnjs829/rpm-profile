package com.jinwon.rpm.profile.domain.terms.dto;

import com.jinwon.rpm.profile.constants.enums.TermsCondition;
import com.jinwon.rpm.profile.constants.enums.TermsType;
import com.jinwon.rpm.profile.domain.terms.Terms;
import com.jinwon.rpm.profile.infra.utils.ModelMapperUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Schema(description = "약관 DTO")
public class TermsDto {

    @Schema(description = "약관 번호")
    private Long termsId;

    @Schema(description = "약관 타입")
    private TermsType type;

    @Schema(description = "약관 종류")
    private TermsCondition condition;

    @Schema(description = "제목")
    private String title;

    @Schema(description = "버전")
    private String version;

    @Schema(description = "고지 일자")
    private LocalDateTime noticeDateTime;

    @Schema(description = "시행 일자")
    private LocalDateTime effectiveDateTime;

    public static TermsDto of(Terms terms) {
        return ModelMapperUtil.map(terms, TermsDto.class);
    }

}
